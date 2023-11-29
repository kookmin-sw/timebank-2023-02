package kookmin.software.capstone2023.timebank.infrastructure.client.kakao.model;

public class KakaoAccount {
    private final Boolean profileNicknameNeedsAgreement;
    private final Boolean profileImageNeedsAgreement;
    private final KakaoAccountProfile profile;

    public KakaoAccount(Boolean profileNicknameNeedsAgreement, Boolean profileImageNeedsAgreement, KakaoAccountProfile profile) {
        this.profileNicknameNeedsAgreement = profileNicknameNeedsAgreement;
        this.profileImageNeedsAgreement = profileImageNeedsAgreement;
        this.profile = profile;
    }

    public Boolean getProfileNicknameNeedsAgreement() {
        return profileNicknameNeedsAgreement;
    }

    public Boolean getProfileImageNeedsAgreement() {
        return profileImageNeedsAgreement;
    }

    public KakaoAccountProfile getProfile() {
        return profile;
    }
}
