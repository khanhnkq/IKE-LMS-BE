package com.ike.platform.tenant

/**
 * ThreadLocal holder cho tenant_id hiện tại.
 * Được set bởi TenantInterceptor (JWT claim) và dùng bởi TenantAwareRepository.
 */
object TenantContext {

    private val holder = ThreadLocal<Long?>()

    fun setTenantId(id: Long) {
        require(id > 0) { "tenant id must be positive, got $id" }
        holder.set(id)
    }

    fun getTenantId(): Long? = holder.get()

    fun clear() = holder.remove()
}
