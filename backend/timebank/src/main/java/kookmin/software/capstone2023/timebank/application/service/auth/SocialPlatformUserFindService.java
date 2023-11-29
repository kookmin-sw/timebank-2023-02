package kookmin.software.capstone2023.timebank.application.service.auth;

import kookmin.software.capstone2023.timebank.domain.model.auth.SocialPlatformType;
import kookmin.software.capstone2023.timebank.infrastructure.client.kakao.KakaoRestClient;
import kookmin.software.capstone2023.timebank.infrastructure.client.kakao.model.GetUserInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SocialPlatformUserFindService {
    private final KakaoRestClient kakaoRestClient;

    @Autowired
    public SocialPlatformUserFindService(KakaoRestClient kakaoRestClient) {
        this.kakaoRestClient = kakaoRestClient;
    }

    @Data
    @AllArgsConstructor
    public static class SocialUser {
        private final SocialPlatformType type;
        private final String id;
    }

    public SocialUser getUser(SocialPlatformType type, String accessToken) {
        if (type == SocialPlatformType.KAKAO) {
            GetUserInfoResponse kakaoUser = kakaoRestClient.getUserInfo("Bearer " + accessToken);
            return new SocialUser(SocialPlatformType.KAKAO, String.valueOf(kakaoUser.getId()));
        }
        return null; // Handle other platform types if needed
    }
}
