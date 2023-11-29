package kookmin.software.capstone2023.timebank.infrastructure.client.kakao;

import kookmin.software.capstone2023.timebank.infrastructure.client.FeignConfiguration;
import kookmin.software.capstone2023.timebank.infrastructure.client.kakao.model.GetUserInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "kakaoRestFeignClient",
        url = "https://kapi.kakao.com",
        configuration = FeignConfiguration.class
)
public interface KakaoRestClient {
    @GetMapping("/v2/user/me")
    GetUserInfoResponse getUserInfo(
            @RequestHeader("Authorization") String authorization
    );
}
