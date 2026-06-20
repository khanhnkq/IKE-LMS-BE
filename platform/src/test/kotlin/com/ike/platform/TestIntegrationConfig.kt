package com.ike.platform

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.context.annotation.Configuration

/**
 * Integration test configuration cho RlsTenantIsolationTest.
 * Cần [@EnableAutoConfiguration] để tạo DataSource, JdbcTemplate, JPA beans.
 * Exclude security/web để không phụ thuộc Keycloak thật.
 */
@Configuration
@EnableAutoConfiguration(
    exclude = [
        SecurityAutoConfiguration::class,
        OAuth2ResourceServerAutoConfiguration::class,
        WebMvcAutoConfiguration::class
    ]
)
class TestIntegrationConfig
