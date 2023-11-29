package kookmin.software.capstone2023.timebank.infrastructure.client.kakao.model;

import java.time.LocalDateTime;

public class GetUserInfoResponse {
    /**
     * 회원번호
     */
    private final Long id;

    /**
     * 카카오계정 정보
     */
    private final KakaoAccount kakaoAccount;

    /**
     * 서비스 연결일시 (UTC)
     */
    private final LocalDateTime connectedAt;

    public GetUserInfoResponse(Long id, KakaoAccount kakaoAccount, LocalDateTime connectedAt) {
        this.id = id;
        this.kakaoAccount = kakaoAccount;
        this.connectedAt = connectedAt;
    }

    public Long getId() {
        return id;
    }

    public KakaoAccount getKakaoAccount() {
        return kakaoAccount;
    }

    public LocalDateTime getConnectedAt() {
        return connectedAt;
    }
}
