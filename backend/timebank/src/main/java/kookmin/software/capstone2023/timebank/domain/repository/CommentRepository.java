package kookmin.software.capstone2023.timebank.domain.repository;

import kookmin.software.capstone2023.timebank.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByInquiryId(Long inquiryId);
    Long countByInquiryId(Long inquiryId);
}
