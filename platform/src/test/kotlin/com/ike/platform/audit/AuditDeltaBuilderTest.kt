package com.ike.platform.audit

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AuditDeltaBuilderTest {

    private val mapper = ObjectMapper()
    private val builder = AuditDeltaBuilder(mapper)

    @Test
    fun `returns empty delta when maps equal`() {
        val before = mapOf("a" to "1", "b" to "2")
        val after = mapOf("a" to "1", "b" to "2")
        val delta = builder.diff(before, after)
        assertThat(delta).isEmpty()
    }

    @Test
    fun `captures changed field with old and new`() {
        val before = mapOf("balance" to "100.00", "name" to "x")
        val after = mapOf("balance" to "150.00", "name" to "x")
        val delta = builder.diff(before, after)
        assertThat(delta).containsEntry("balance", mapOf("old" to "100.00", "new" to "150.00"))
        assertThat(delta).doesNotContainKey("name")
    }

    @Test
    fun `captures added field as new only`() {
        val delta = builder.diff(mapOf("a" to "1"), mapOf("a" to "1", "b" to "2"))
        assertThat(delta).containsEntry("b", mapOf("old" to null, "new" to "2"))
    }

    @Test
    fun `captures removed field as old only`() {
        val delta = builder.diff(mapOf("a" to "1", "b" to "2"), mapOf("a" to "1"))
        assertThat(delta).containsEntry("b", mapOf("old" to "2", "new" to null))
    }

    @Test
    fun `serializes delta to JSON string`() {
        val delta = mapOf("balance" to mapOf("old" to "100.00", "new" to "150.00"))
        val json = builder.toJson(delta)
        assertThat(json).contains("balance").contains("old").contains("new")
    }
}
