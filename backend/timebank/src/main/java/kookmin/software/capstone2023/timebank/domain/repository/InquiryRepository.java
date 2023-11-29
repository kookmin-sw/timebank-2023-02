package kookmin.software.capstone2023.timebank.domain.repository;

import kookmin.software.capstone2023.timebank.domain.model.Inquiry;
import kookmin.software.capstone2023.timebank.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    List<Inquiry> findByUser(User user);

    List<Inquiry> findByTitleContainingIgnoreCase(String title);

    List<Inquiry> findByTitleContainingIgnoreCaseAndUserId(String title, Long userId);

    List<Inquiry> findByInquiryDateBetween(ZonedDateTime start, ZonedDateTime end);

    List<Inquiry> findByInquiryDateBetweenAndUserId(ZonedDateTime start, ZonedDateTime end, Long userId);

    @Query(
            "SELECT i FROM Inquiry i " +
                    "WHERE (:title is null or i.title LIKE %:title%) " +
                    "AND (:startDate is null or i.inquiryDate >= :startDate) " +
                    "AND (:endDate is null or i.inquiryDate <= :endDate) " +
                    "AND (:userId is null or i.user.id = :userId)"
    )
    List<Inquiry> findAllByTitleAndPeriodAndUserId(
            @Param("title") String title,
            @Param("startDate") ZonedDateTime startDate,
            @Param("endDate") ZonedDateTime endDate,
            @Param("userId") Long userId
    );
}
