package com.ike.platform.persistence

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant

/**
 * Base entity cho MỌI bảng nghiệp vụ IKE v4.0.0.
 * Đảm bảo 3 metadata fields (đặc tả Chương 3):
 *   - created_at: tự populate
 *   - updated_at: tự populate
 *   - deleted_at: soft-delete (null = active, có giá trị = đã xóa)
 *
 * @SQLRestriction tự động filter deleted_at IS NULL trong mọi JPQL query.
 * @SQLDelete override DELETE command thành SET deleted_at = NOW().
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@SQLDelete(sql = "UPDATE {h-schema}{h-entity} SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
abstract class BaseEntity : Auditable {

    @CreatedDate
    override var createdAt: Instant? = null

    @LastModifiedDate
    override var updatedAt: Instant? = null

    override var deletedAt: Instant? = null
}
