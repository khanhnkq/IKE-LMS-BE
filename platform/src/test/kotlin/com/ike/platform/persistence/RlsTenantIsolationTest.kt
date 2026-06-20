package com.ike.platform.persistence

import com.ike.platform.TestIntegrationConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
@SpringBootTest
@ContextConfiguration(classes = [TestIntegrationConfig::class])
class RlsTenantIsolationTest {

    companion object {
        @Container
        val postgres = PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))
            .withDatabaseName("ike").withUsername("ike").withPassword("ike")

        @DynamicPropertySource
        @JvmStatic
        fun props(r: DynamicPropertyRegistry) {
            r.add("spring.datasource.url", postgres::getJdbcUrl)
            r.add("spring.datasource.username", postgres::getUsername)
            r.add("spring.datasource.password", postgres::getPassword)
            r.add("spring.jpa.hibernate.ddl-auto") { "validate" }
            r.add("spring.flyway.locations") { "classpath:db/migration" }
            r.add("spring.flyway.enabled") { "true" }
            r.add("spring.security.oauth2.resourceserver.jwt.issuer-uri") { "http://localhost:9999/realms/ike" }
        }
    }

    @Autowired
    private lateinit var jdbc: JdbcTemplate

    @Test
    fun `tenant 1 cannot see tenant 2 rows`() {
        // requires tenants seeded (Flyway V1 creates only roles; tenants must exist)
        jdbc.execute("INSERT INTO tenants(id, name, code) VALUES (1, 'T1', 't1'), (2, 'T2', 't2')")
        jdbc.execute("INSERT INTO centers(id, tenant_id, name, code) VALUES (10, 1, 'C1', 'c1'), (20, 2, 'C2', 'c2')")

        // session tenant = 1
        jdbc.execute("SET app.tenant_id = '1'")
        val rows = jdbc.queryForList("SELECT id, tenant_id FROM centers")
        assertThat(rows).hasSize(1)
        assertThat(rows[0]["tenant_id"]).isEqualTo(1L)
    }
}
