package com.ike.platform.persistence

import java.time.Instant

interface Auditable {
    val createdAt: Instant?
    val updatedAt: Instant?
    val deletedAt: Instant?
}
