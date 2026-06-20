package com.ike.app

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IkeApplicationSmokeTest {

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
            r.add("spring.flyway.locations") { "classpath:db/migration" }
            r.add("spring.flyway.enabled") { "false" }
            r.add("spring.security.oauth2.resourceserver.jwt.issuer-uri") { "http://localhost:9999/realms/ike" }
        }
    }

    @Autowired
    private lateinit var rest: TestRestTemplate

    @Test
    fun `actuator health returns UP`() {
        val resp = rest.getForEntity("/actuator/health", String::class.java)
        assertThat(resp.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(resp.body).contains("UP")
    }
}
