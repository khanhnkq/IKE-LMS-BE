package com.ike.platform

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 * Test configuration cho platform module tests.
 * [@AutoConfigurationPackage] đăng ký base package cho auto-configuration
 * để @DataJpaTest và các slice test khác có thể scan entities/repositories.
 * Không cần @EnableAutoConfiguration vì slice test tự import config riêng.
 */
@SpringBootConfiguration
@AutoConfigurationPackage(basePackages = ["com.ike.platform"])
@EntityScan(basePackages = ["com.ike.platform.persistence"])
@EnableJpaRepositories(basePackages = ["com.ike.platform.persistence"])
class TestPlatformConfig
