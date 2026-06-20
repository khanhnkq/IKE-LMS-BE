package com.ike.platform.persistence

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource

@Repository
interface SampleRepository : JpaRepository<SampleEntity, Long>

@Testcontainers
@DataJpaTest
class BaseEntityTest {

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
            r.add("spring.jpa.hibernate.ddl-auto") { "update" }
            r.add("spring.flyway.enabled") { "false" }
        }
    }

    @Autowired
    private lateinit var em: TestEntityManager

    @Autowired
    private lateinit var repo: SampleRepository

    @Test
    fun `saved entity has created_at and updated_at populated`() {
        val saved = repo.save(SampleEntity.create())
        em.flush()
        em.clear()

        val reloaded = repo.findById(saved.id!!).get()
        assertThat(reloaded.createdAt).isNotNull()
        assertThat(reloaded.updatedAt).isNotNull()
    }

    @Test
    fun `soft-delete sets deleted_at and hides from findAll`() {
        val saved = repo.save(SampleEntity.create())
        em.flush()
        em.clear()

        repo.delete(saved)
        em.flush()
        em.clear()

        assertThat(repo.findAll()).isEmpty()
    }
}
