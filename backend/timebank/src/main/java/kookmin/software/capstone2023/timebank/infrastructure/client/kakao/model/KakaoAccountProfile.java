package kookmin.software.capstone2023.timebank.infrastructure.client.kakao.model;

public class KakaoAccountProfile {
    /**
     * 닉네임
     *
     * 필요한 동의 항목: 프로필 정보(닉네임/프로필 사진) 또는 닉네임
     */
    private final String nickname;

    /**
     * 프로필 이미지 URL
     * 640px * 640px 또는 480px * 480px
     *
     * 필요한 동의 항목: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
     *
     */
    private final String profileImageUrl;

    /**
     * 프로필 미리보기 이미지 URL
     * 110px * 110px 또는 100px * 100px
     *
     * 필요한 동의 항목: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
     */
    private final String thumbnailImageUrl;

    /**
     * 프로필 사진 URL이 기본 프로필 사진 URL인지 여부
     *
     * 사용자가 등록한 프로필 사진이 없을 경우, 기본 프로필 사진 제공
     * true: 기본 프로필 사진
     * false: 사용자가 등록한 프로필 사진
     *
     * 필요한 동의 항목: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
     */
    private final Boolean isDefaultImage;

    public KakaoAccountProfile(String nickname, String profileImageUrl, String thumbnailImageUrl, Boolean isDefaultImage) {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.isDefaultImage = isDefaultImage;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public Boolean getIsDefaultImage() {
        return isDefaultImage;
    }
}
