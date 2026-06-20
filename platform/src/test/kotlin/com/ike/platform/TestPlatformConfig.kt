package com.ike.platform

import org.springframework.boot.SpringBootConfiguration

/**
 * Test configuration cho platform module tests.
 * Cần thiết vì platform là shared module, không phải boot app.
 */
@SpringBootConfiguration
class TestPlatformConfig
