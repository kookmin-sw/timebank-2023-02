package kookmin.software.capstone2023.timebank.presentation.api.v1

import kookmin.software.capstone2023.timebank.application.exception.UnauthorizedException
import kookmin.software.capstone2023.timebank.application.service.inqui.CommentService
import kookmin.software.capstone2023.timebank.domain.model.AccountType
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@UserAuthentication
@RestController
@RequestMapping("/api/v1/inquiries/{id}/comments")
class CommentController(
    private val commentService: CommentService,
) {
    /**
     * 댓글 작성(User)
     */
    @PostMapping
    fun createComment(
        @RequestAttribute(RequestAttributes.USER_CONTEXT) userContext: UserContext,
        @PathVariable id: Long,
        @RequestBody request: CommentService.CommentCreateRequest,
    ): CommentService.CommentDto {
        return commentService.createComment(id, request, userContext.userId, userContext.accountType)
    }

    /**
     * 댓글 전체 조회
     */
    @GetMapping
    fun getCommentsByInquiryId(@PathVariable id: Long): List<CommentService.CommentDto> {
        return commentService.getCommentByInquiryId(id)
    }

    /**
     * 댓글 수정
     */
    @PutMapping("/users/{userId}/{commentId}")
    fun updateComment(
        @RequestAttribute(RequestAttributes.USER_CONTEXT) userContext: UserContext,
        @PathVariable id: Long,
        @PathVariable userId: Long,
        @PathVariable commentId: Long,
        @RequestBody request: CommentService.CommentUpdateRequest,
    ): CommentService.CommentDto {
        if (userContext.userId != userId) {
            throw UnauthorizedException(message = "수정 권한이 없습니다.")
        }
        return commentService.updateComment(commentId, request)
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/users/{userId}/{commentId}")
    fun deleteCommentById(
        @RequestAttribute(RequestAttributes.USER_CONTEXT) userContext: UserContext,
        @PathVariable userId: Long,
        @PathVariable commentId: Long,
        @PathVariable id: Long,
    ): ResponseEntity<Unit> {
        if (userContext.accountType == AccountType.INDIVIDUAL) {
            if (userContext.userId != userId) {
                throw UnauthorizedException(message = "삭제 권한이 없습니다.")
            }
        }
        commentService.deleteCommentByCommentId(id, commentId)
        return ResponseEntity.noContent().build()
    }
}
