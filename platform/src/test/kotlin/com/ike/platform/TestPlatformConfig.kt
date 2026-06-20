package com.ike.platform

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration

/**
 * Test configuration cho platform module tests.
 * Cần [@EnableAutoConfiguration] để kích hoạt JPA/DataSource/JdbcTemplate beans.
 * Exclude security/web auto-config để không phụ thuộc Keycloak thật.
 */
@SpringBootConfiguration
@EnableAutoConfiguration(
    exclude = [
        SecurityAutoConfiguration::class,
        OAuth2ResourceServerAutoConfiguration::class,
        WebMvcAutoConfiguration::class
    ]
)
class TestPlatformConfig
