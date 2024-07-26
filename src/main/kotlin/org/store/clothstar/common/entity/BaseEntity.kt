package org.store.clothstar.common.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class) //이벤트 발생할때마다 Auditing기능 활성화
open class BaseEntity(
    @CreatedDate
    private var createdAt: LocalDateTime? = LocalDateTime.now(),
    @LastModifiedDate
    private var updatedAt: LocalDateTime? = LocalDateTime.now(),
    private var deletedAt: LocalDateTime? = null,
) {
    fun updateDeletedAt() {
        this.deletedAt = LocalDateTime.now()
    }
}