package kookmin.software.capstone2023.timebank.application.service.auth.model;

import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.domain.model.auth.AuthenticationType;
import kookmin.software.capstone2023.timebank.domain.model.auth.SocialPlatformType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialAuthenticationRequest extends AuthenticationRequest {
    private final SocialPlatformType socialPlatformType;
    private final String accessToken;

    public SocialAuthenticationRequest(AccountType accountType, SocialPlatformType socialPlatformType, String accessToken) {
        super(AuthenticationType.SOCIAL, accountType);
        this.socialPlatformType = socialPlatformType;
        this.accessToken = accessToken;
    }
}