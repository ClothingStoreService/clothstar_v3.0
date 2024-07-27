package org.store.clothstar.common.domain;

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseTimeEntity {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime? = null // 생성일시

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    var modifiedAt: LocalDateTime? = null // 수정일시

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    var deletedAt: LocalDateTime? = null // 삭제일시

    protected fun setModifiedAt(modifiedAt: LocalDateTime) {
        this.modifiedAt = modifiedAt
    }

    protected fun setDeletedAt(deletedAt: LocalDateTime) {
        this.deletedAt = deletedAt
    }

    override fun toString(): String {
        return "BaseTimeEntity(createdAt=$createdAt, modifiedAt=$modifiedAt, deletedAt=$deletedAt)"
    }
}
