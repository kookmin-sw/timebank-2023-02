package kookmin.software.capstone2023.timebank.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.ZoneId
import java.time.ZonedDateTime

@Entity
@Table(name = "inquiry")
class Inquiry(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(nullable = false)
    var inquiryDate: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul")),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var replyStatus: InquiryStatus = InquiryStatus.PENDING,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
) {
    @Column(name = "user_id", insertable = false, updatable = false)
    val userId: Long = user.id
}
