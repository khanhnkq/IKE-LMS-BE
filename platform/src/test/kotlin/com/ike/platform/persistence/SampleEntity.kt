package com.ike.platform.persistence

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction

/**
 * Entity mẫu trong cùng test package để verify BaseEntity behavior.
 * Kế thừa @EntityListeners(AuditingEntityListener) từ BaseEntity (JPA inheritance).
 */
@Entity
@SQLDelete(sql = "UPDATE sample_entity SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
class SampleEntity private constructor() : BaseEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    companion object {
        fun create() = SampleEntity()
    }
}
