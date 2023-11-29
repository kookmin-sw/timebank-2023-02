package kookmin.software.capstone2023.timebank.presentation.api.v1;

import kookmin.software.capstone2023.timebank.application.exception.UnauthorizedException;
import kookmin.software.capstone2023.timebank.application.service.inqui.CommentService;
import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@UserAuthentication
@RestController
@RequestMapping("/api/v1/inquiries/{id}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 작성(User)
     */
    @PostMapping
    public CommentService.CommentDto createComment(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @PathVariable Long id,
            @RequestBody CommentService.CommentCreateRequest request) {
        return commentService.createComment(id, request, userContext.getUserId(), userContext.getAccountType());
    }

    /**
     * 댓글 전체 조회
     */
    @GetMapping
    public List<CommentService.CommentDto> getCommentsByInquiryId(@PathVariable Long id) {
        return commentService.getCommentByInquiryId(id);
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/users/{userId}/{commentId}")
    public CommentService.CommentDto updateComment(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @PathVariable Long id,
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @RequestBody CommentService.CommentUpdateRequest request) {
        if (userContext.getUserId() != userId) {
            throw new UnauthorizedException("수정 권한이 없습니다.");
        }
        return commentService.updateComment(commentId, request);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/users/{userId}/{commentId}")
    public ResponseEntity<Void> deleteCommentById(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @PathVariable Long userId,
            @PathVariable Long commentId,
            @PathVariable Long id) {
        if (userContext.getAccountType() == AccountType.INDIVIDUAL) {
            if (userContext.getUserId() != userId) {
                throw new UnauthorizedException("삭제 권한이 없습니다.");
            }
        }
        commentService.deleteCommentByCommentId(id, commentId);
        return ResponseEntity.noContent().build();
    }
}
