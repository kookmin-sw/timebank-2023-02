package kookmin.software.capstone2023.timebank.domain.model

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.ZoneId

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseTimeEntity(
    /**
     * 생성 시간 (UTC)
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")),

    /**
     * 마지막 수정 시간 (UTC)
     */
    @LastModifiedDate
    @Column(nullable = false, updatable = true)
    var updatedAt: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")),
)
