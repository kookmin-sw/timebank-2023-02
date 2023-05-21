package kookmin.software.capstone2023.timebank.domain.repository

import kookmin.software.capstone2023.timebank.domain.model.Inquiry
import kookmin.software.capstone2023.timebank.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime

@Repository
interface InquiryRepository : JpaRepository<Inquiry, Long> {
    fun findByUser(user: User): List<Inquiry>
    fun findByTitleContainingIgnoreCase(title: String): List<Inquiry>
    fun findByTitleContainingIgnoreCaseAndUserId(title: String, userId: Long): List<Inquiry>
    fun findByInquiryDateBetween(start: ZonedDateTime, end: ZonedDateTime): List<Inquiry>
    fun findByInquiryDateBetweenAndUserId(start: ZonedDateTime, end: ZonedDateTime, userId: Long): List<Inquiry>

    @Query(
        "SELECT i FROM Inquiry i " +
            "WHERE (:title is null or i.title LIKE %:title%) " +
            "AND (:startDate is null or i.inquiryDate >= :startDate) " +
            "AND (:endDate is null or i.inquiryDate <= :endDate) " +
            "AND (:userId is null or i.user.id = :userId)",
    )
    fun findAllByTitleAndPeriodAndUserId(
        @Param("title") title: String?,
        @Param("startDate") startDate: ZonedDateTime?,
        @Param("endDate") endDate: ZonedDateTime?,
        @Param("userId") userId: Long?,
    ): List<Inquiry>
}
