package kookmin.software.capstone2023.timebank.presentation.api.v1.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import kookmin.software.capstone2023.timebank.application.service.auth.model.AuthenticationRequest;
import kookmin.software.capstone2023.timebank.application.service.auth.model.PasswordAuthenticationRequest;
import kookmin.software.capstone2023.timebank.application.service.auth.model.SocialAuthenticationRequest;
import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.domain.model.auth.AuthenticationType;
import kookmin.software.capstone2023.timebank.domain.model.auth.SocialPlatformType;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "authenticationType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserLoginRequestData.SocialLoginRequestData.class, name = "social"),
        @JsonSubTypes.Type(value = UserLoginRequestData.PasswordLoginRequestData.class, name = "password")
})
public abstract class UserLoginRequestData {

    private final AuthenticationType authenticationType;

    public UserLoginRequestData(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public abstract AuthenticationRequest toAuthenticationRequest();

    @JsonTypeName("social")
    public static class SocialLoginRequestData extends UserLoginRequestData {

        private final SocialPlatformType socialPlatformType;

        @NotBlank(message = "액세스 토큰은 필수입니다.")
        private final String accessToken;

        public SocialLoginRequestData(SocialPlatformType socialPlatformType, String accessToken) {
            super(AuthenticationType.SOCIAL);
            this.socialPlatformType = socialPlatformType;
            this.accessToken = accessToken;
        }

        public SocialPlatformType getSocialPlatformType() {
            return socialPlatformType;
        }

        public String getAccessToken() {
            return accessToken;
        }

        @Override
        public AuthenticationRequest toAuthenticationRequest() {
            return new SocialAuthenticationRequest(
                    AccountType.INDIVIDUAL,
                    socialPlatformType,
                    accessToken
            );
        }
    }

    @JsonTypeName("password")
    public static class PasswordLoginRequestData extends UserLoginRequestData {

        @NotBlank(message = "아이디는 필수입니다.")
        private final String username;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private final String password;

        public PasswordLoginRequestData(String username, String password) {
            super(AuthenticationType.PASSWORD);
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        @Override
        public AuthenticationRequest toAuthenticationRequest() {
            return new PasswordAuthenticationRequest(
                    AccountType.INDIVIDUAL,
                    username,
                    password
            );
        }
    }
}
