package com.ike.platform.tenant

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

/**
 * Resolve tenant_id từ Keycloak JWT claim → TenantContext ThreadLocal.
 * JWT payload chứa claim "tenant_id" (map qua protocol mapper ở Keycloak).
 */
@Component
class TenantInterceptor : HandlerInterceptor {

    override fun preHandle(req: HttpServletRequest, resp: HttpServletResponse, handler: Any): Boolean {
        val auth = SecurityContextHolder.getContext()?.authentication?.principal
        val tenantId = (auth as? Jwt)
            ?.getClaimAsString("tenant_id")
            ?.toLongOrNull()
        tenantId?.let { TenantContext.setTenantId(it) }
        return true
    }

    override fun afterCompletion(
        req: HttpServletRequest, resp: HttpServletResponse, handler: Any, ex: Exception?
    ) {
        TenantContext.clear()
    }
}
