package kookmin.software.capstone2023.timebank.presentation.api.v1;

import kookmin.software.capstone2023.timebank.application.exception.UnauthorizedException;
import kookmin.software.capstone2023.timebank.application.service.inqui.InquiryService;
import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.domain.model.Period;
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@UserAuthentication
@RestController
@RequestMapping("/api/v1/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    /**
     * 문의 작성
     */
    @PostMapping
    public InquiryService.InquiryDto createInquiry(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @RequestBody InquiryService.InquiryCreateRequest request) {
        return inquiryService.createInquiry(request, userContext.getUserId());
    }

    /**
     * 문의 전체 조회
     */
    @GetMapping
    public List<InquiryService.InquiryDto> getInquiries(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext) {
        if (userContext.getAccountType() != AccountType.BRANCH) {
            throw new UnauthorizedException("접근 권한이 없습니다.");
        }
        return inquiryService.getInquiries();
    }

    /**
     * 문의ID겁색 조회
     *
     */
    @GetMapping("/{id}")
    public InquiryService.InquiryDto getInquiryById(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @PathVariable Long id) {
        if (userContext.getAccountType() != AccountType.BRANCH) {
            throw new UnauthorizedException("접근 권한이 없습니다.");
        }
        return inquiryService.getInquiryById(id);
    }

    /**
     * userId검색 조회
     */
    @GetMapping("/users/{userId}")
    public List<InquiryService.InquiryDto> getInquiriesByUserId(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @PathVariable Long userId) {
        if (userContext.getAccountType() == AccountType.INDIVIDUAL) {
            if (userContext.getUserId() != userId) {
                throw new UnauthorizedException("접근권한이 없습니다.");
            }
        }
        return inquiryService.getInquiriesByUserId(userId);
    }

    /**
     * 문의 기간 조회 for branch
     */
    @GetMapping("/period")
    public List<InquiryService.InquiryDto> getInquiriesByPeriod(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @RequestParam("period") Period period) {
        if (userContext.getAccountType() != AccountType.BRANCH) {
            throw new UnauthorizedException("접근 권한이 없습니다.");
        }
        return inquiryService.getInquiriesByPeriod(period);
    }

    /**
     * 문의 기간 조회 for user
     */
    @GetMapping("/users/{userId}/period")
    public List<InquiryService.InquiryDto> getUserInquiriesByPeriod(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @RequestParam("period") Period period,
            @PathVariable Long userId) {
        if (userContext.getUserId() != userId) {
            throw new UnauthorizedException("접근 권한이 없습니다.");
        }
        return inquiryService.getUserInquiriesByPeriod(period, userId);
    }

    /**
     * 문의 제목 조회 for branch
     */
    @GetMapping("/search")
    public List<InquiryService.InquiryDto> getInquiriesByTitle(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @RequestParam("title") String title) {
        if (userContext.getAccountType() != AccountType.BRANCH) {
            throw new UnauthorizedException("접근 권한이 없습니다.");
        }
        return inquiryService.getInquiryByTitle(title);
    }

    /**
     * 문의 조회 (동시 조건 검색)
     */
    @GetMapping("/multisearch")
    public List<InquiryService.InquiryDto> searchInquiries(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "period", required = false) Period period,
            @RequestParam(value = "userId", required = false) Long userId) {
        if (userContext.getAccountType() == AccountType.INDIVIDUAL) {
            if (userContext.getUserId() != userId) {
                throw new UnauthorizedException("접근 권한이 없습니다.");
            }
        }
        return inquiryService.searchInquiries(title, period, userId);
    }

    /**
     * 문의 제목 조회 for user
     */
    @GetMapping("/users/{userId}/search")
    public List<InquiryService.InquiryDto> getUserInquiriesByTitle(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @RequestParam("title") String title,
            @PathVariable Long userId) {
        if (userContext.getUserId() != userId) {
            throw new UnauthorizedException("접근 권한이 없습니다.");
        }
        return inquiryService.getUserInquiryByTitle(title, userId);
    }

    /**
     * 문의 수정
     */
    @PutMapping("/users/{userId}/{id}")
    public InquiryService.InquiryDto updateInquiry(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @PathVariable Long id,
            @PathVariable Long userId,
            @RequestBody InquiryService.InquiryUpdateRequest request) {
        if (userContext.getUserId() != userId) {
            throw new UnauthorizedException("수정 권한이 없습니다.");
        }
        return inquiryService.updateInquiry(id, request);
    }

    /**
     * 문의 삭제
     */
    @DeleteMapping("/users/{userId}/{inquiryId}")
    public ResponseEntity<Void> deleteInquiryByUserId(
            @PathVariable Long userId,
            @PathVariable Long inquiryId) {
        inquiryService.deleteInquiryByUserId(userId, inquiryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 문의 상태 변경 for branch
     */
    @PutMapping("/{id}/status")
    public InquiryService.InquiryDto updateInquiryStatus(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @PathVariable Long id,
            @RequestBody InquiryService.InquiryStatusUpdateRequest request) {
        if (userContext.getAccountType() != AccountType.BRANCH) {
            throw new UnauthorizedException("접근 권한이 없습니다.");
        }
        return inquiryService.updateInquiryStatus(id, request.getStatus());
    }
}
