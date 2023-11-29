package kookmin.software.capstone2023.timebank.application.service.inqui;

import kookmin.software.capstone2023.timebank.application.exception.UnauthorizedException;
import kookmin.software.capstone2023.timebank.domain.model.*;
import kookmin.software.capstone2023.timebank.domain.repository.CommentRepository;
import kookmin.software.capstone2023.timebank.domain.repository.InquiryRepository;
import kookmin.software.capstone2023.timebank.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final InquiryRepository inquiryRepository;
    private final UserJpaRepository userJpaRepository;
    private final CommentRepository commentRepository;

    /**
     * Dto 클래스
     */
    public static class CommentDto {
        public Long commentid;
        public Long commentSeq;
        public String content;
        public ZonedDateTime commentDate;
        public Long userId;
        public Long inquiryId;
    }

    /**
     * 댓글 생성 Dto(User)
     */
    @RequiredArgsConstructor
    public static class CommentCreateRequest {
        public final String content;
        public final ZonedDateTime commentDate;
    }

    /**
     * 댓글 수정 Dto
     */
    @RequiredArgsConstructor
    public static class CommentUpdateRequest {
        public final String content;
        public final ZonedDateTime commentDate;
    }

    /**
     * 댓글 생성 service (user)
     */
    @Transactional
    public CommentDto createComment(
            Long inquiryId,
            CommentCreateRequest request,
            Long userId,
            AccountType accountType) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found with id: " + userId));

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new UnauthorizedException("Inquiry not found with id: " + inquiryId));

        long previousCommentCount = commentRepository.countByInquiryId(inquiryId);
        long newCommentId = previousCommentCount + 1;

        Comment comment = new Comment(
                request.content,
                user,
                inquiry,
                request.commentDate,
                newCommentId
        );

        if (accountType == AccountType.BRANCH) {
            inquiry.setReplyStatus(InquiryStatus.ANSWERED);
        } else {
            if (!userId.equals(inquiry.getUserId())) {
                throw new UnauthorizedException("접근 권한이 없습니다.");
            } else {
                inquiry.setReplyStatus(InquiryStatus.REPENDING);
            }
        }

        var savedComment = commentRepository.save(comment);
        return commentToDto(savedComment);
    }

    /**
     * 문의 id 검색 service
     */
    public List<CommentDto> getCommentByInquiryId(Long id) {
        var comments = commentRepository.findByInquiryId(id);
        return comments.stream().map(this::commentToDto).collect(Collectors.toList());
    }

    /**
     * 댓글 수정 service
     */
    public CommentDto updateComment(Long id, CommentUpdateRequest request) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new UnauthorizedException("Comment not found with id: " + id));

        comment.setContent(request.content != null ? request.content : comment.getContent());
        comment.setCommentDate(request.commentDate != null ? request.commentDate : comment.getCommentDate());

        var updatedComment = commentRepository.save(comment);
        return commentToDto(updatedComment);
    }

    /**
     * 댓글 삭제 service
     */
    public void deleteCommentByCommentId(Long inquiryId, Long commentId) {
        var inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new UnauthorizedException("Inquiry not found with id: " + inquiryId));

        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new UnauthorizedException("Comment not found with id: " + commentId));

        if (!comment.getInquiry().getId().equals(inquiry.getId())) {
            throw new UnauthorizedException("User does not have permission to delete this inquiry");
        }

        commentRepository.deleteById(commentId);
    }

    private CommentDto commentToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.commentid = comment.getId();
        dto.content = comment.getContent();
        dto.commentDate = comment.getCommentDate();
        dto.inquiryId = comment.getInquiryId();
        dto.userId = comment.getUserId();
        dto.commentSeq = comment.getCommentSeq();
        return dto;
    }
}
