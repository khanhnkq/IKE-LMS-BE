package com.ike.platform.audit

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

/**
 * Tính JSON Delta (diff) cho bảng audit_logs theo đặc tả Chương 1/3:
 * chỉ ghi các trường thực sự biến đổi, tránh data bloat.
 * Output: map<fieldName, {old, new}> — chỉ chứa keys có thay đổi.
 */
@Component
class AuditDeltaBuilder(private val mapper: ObjectMapper) {

    fun diff(before: Map<String, Any?>, after: Map<String, Any?>): Map<String, Map<String, Any?>> {
        val delta = mutableMapOf<String, Map<String, Any?>>()
        val allKeys = before.keys + after.keys
        for (key in allKeys) {
            val oldVal = before[key]
            val newVal = after[key]
            if (oldVal != newVal) {
                delta[key] = mapOf("old" to oldVal, "new" to newVal)
            }
        }
        return delta
    }

    /** Serialize delta ra JSONB-ready string cho cột changed_fields. */
    fun toJson(delta: Map<String, Map<String, Any?>>): String = mapper.writeValueAsString(delta)
}
