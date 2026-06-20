package com.ike.platform.tenant

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class TenantContextTest {

    @AfterEach
    fun clear() = TenantContext.clear()

    @Test
    fun `set and get tenant id on current thread`() {
        TenantContext.setTenantId(42L)
        assertThat(TenantContext.getTenantId()).isEqualTo(42L)
    }

    @Test
    fun `get returns null when no tenant set`() {
        assertThat(TenantContext.getTenantId()).isNull()
    }

    @Test
    fun `clear removes tenant id`() {
        TenantContext.setTenantId(7L)
        TenantContext.clear()
        assertThat(TenantContext.getTenantId()).isNull()
    }

    @Test
    fun `setTenantId rejects non-positive id`() {
        assertThrows<IllegalArgumentException> { TenantContext.setTenantId(0L) }
        assertThrows<IllegalArgumentException> { TenantContext.setTenantId(-1L) }
    }
}
