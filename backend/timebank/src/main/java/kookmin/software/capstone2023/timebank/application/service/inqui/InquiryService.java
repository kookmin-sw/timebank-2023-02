package kookmin.software.capstone2023.timebank.application.service.inqui;

import kookmin.software.capstone2023.timebank.application.exception.NotFoundException;
import kookmin.software.capstone2023.timebank.application.exception.UnauthorizedException;
import kookmin.software.capstone2023.timebank.domain.model.Inquiry;
import kookmin.software.capstone2023.timebank.domain.model.InquiryStatus;
import kookmin.software.capstone2023.timebank.domain.model.Period;
import kookmin.software.capstone2023.timebank.domain.repository.InquiryRepository;
import kookmin.software.capstone2023.timebank.domain.repository.UserJpaRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserJpaRepository userJpaRepository;

    /**
     * Dto 클래스 정의
     */

    @RequiredArgsConstructor
    public static class InquiryDto {
        public final Long inquiryid;
        public final String title;
        public final String content;
        public final ZonedDateTime inquiryDate;
        public final InquiryStatus replyStatus;
        public final Long userId;
        public final String username;
    }

    /**
     * 문의 생성Dto
     */
    @RequiredArgsConstructor
    public static class InquiryCreateRequest {
        public final String title;
        public final String content;
        public final ZonedDateTime inquiryDate;
    }

    /**
     * 수정 Dto
     */
    @RequiredArgsConstructor
    public static class InquiryUpdateRequest {
        public final String updateTitle;
        public final String updateContent;
        public final ZonedDateTime updateDate;
    }

    /**
     * 문의 상태 Dto
     */
    @RequiredArgsConstructor
    @Data
    public static class InquiryStatusUpdateRequest {
        public final InquiryStatus status;
    }

    /**
     * 서비스 메소드 정의
     */

    /**
     * 문의 생성 service
     */
    @Transactional
    public InquiryDto createInquiry(InquiryCreateRequest request, Long userId) {
        var user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        var inquiry = new Inquiry(
                request.title,
                request.content,
                user,
                request.inquiryDate
        );

        var savedInquiry = inquiryRepository.save(inquiry);
        return inquiryToDto(savedInquiry);
    }

    /**
     * 전체 문의 검색 service
     */
    public List<InquiryDto> getInquiries() {
        var inquiries = inquiryRepository.findAll();
        return inquiries.stream().map(this::inquiryToDto).collect(Collectors.toList());
    }

    /**
     * 문의ID검색 service
     */
    public InquiryDto getInquiryById(Long id) {
        var inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inquiry not found with id: " + id));
        return inquiryToDto(inquiry);
    }

    /**
     * userId 검색 service
     */
    public List<InquiryDto> getInquiriesByUserId(Long userId) {
        var user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        var inquiries = inquiryRepository.findByUser(user);
        return inquiries.stream().map(this::inquiryToDto).collect(Collectors.toList());
    }

    /**
     * 기간별 조회 for branch
     */
    public List<InquiryDto> getInquiriesByPeriod(Period period) {
        var end = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        var start = end.minusMonths(period.getMonths());

        var inquiries = inquiryRepository.findByInquiryDateBetween(start, end);
        return inquiries.stream().map(this::inquiryToDto).collect(Collectors.toList());
    }

    /**
     * 기간별 조회 for user
     */
    public List<InquiryDto> getUserInquiriesByPeriod(Period period, Long userId) {
        var end = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        var start = end.minusMonths(period.getMonths());

        var inquiries = inquiryRepository.findByInquiryDateBetweenAndUserId(start, end, userId);
        return inquiries.stream().map(this::inquiryToDto).collect(Collectors.toList());
    }

    /**
     * 문의 제목 검색 for branch
     */
    public List<InquiryDto> getInquiryByTitle(String title) {
        var inquiries = inquiryRepository.findByTitleContainingIgnoreCase(title);
        return inquiries.stream().map(this::inquiryToDto).collect(Collectors.toList());
    }

    /**
     * 문의 제목 검색 for user
     */
    public List<InquiryDto> getUserInquiryByTitle(String title, Long userId) {
        var inquiries = inquiryRepository.findByTitleContainingIgnoreCaseAndUserId(title, userId);
        return inquiries.stream().map(this::inquiryToDto).collect(Collectors.toList());
    }

    /**
     * 문의 수정 service
     */
    public InquiryDto updateInquiry(Long id, InquiryUpdateRequest request) {
        var inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inquiry not found with id: " + id));

        inquiry.setContent(request.updateContent != null ? request.updateContent : inquiry.getContent());
        inquiry.setTitle(request.updateTitle);
        inquiry.setInquiryDate(request.updateDate != null ? request.updateDate : inquiry.getInquiryDate());

        var updatedInquiry = inquiryRepository.save(inquiry);
        return inquiryToDto(updatedInquiry);
    }

    /**
     * 문의삭제 service
     */
    public void deleteInquiryByUserId(Long userId, Long inquiryId) {
        var user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        var inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new NotFoundException("Inquiry not found with id: " + inquiryId));

        if (!user.getId().equals(inquiry.getUser().getId())) {
            throw new UnauthorizedException("삭제 권한이 없습니다.");
        }

        inquiryRepository.deleteById(inquiryId);
    }

    /**
     * 문의 상태 변경 for branch
     */
    public InquiryDto updateInquiryStatus(Long id, InquiryStatus status) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당 문의를 찾을 수 없습니다."));

        inquiry.setReplyStatus(status);
        var updatedInquiry = inquiryRepository.save(inquiry);
        return inquiryToDto(updatedInquiry);
    }

    /**
     * 문의 search (동시 조건 검색)
     */
    public List<InquiryDto> searchInquiries(String title, Period period, Long userId) {
        var end = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        var start = period != null ? end.minusMonths(period.getMonths()) : null;

        var inquiries = inquiryRepository.findAllByTitleAndPeriodAndUserId(title, start, end, userId);
        return inquiries.stream().map(this::inquiryToDto).collect(Collectors.toList());
    }

    /**
     * 유틸 메소드 정의
     */
    private InquiryDto inquiryToDto(Inquiry inquiry) {
        return new InquiryDto(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getInquiryDate(),
                inquiry.getReplyStatus(),
                inquiry.getUser().getId(),
                inquiry.getUser().getName()
        );
    }
}
