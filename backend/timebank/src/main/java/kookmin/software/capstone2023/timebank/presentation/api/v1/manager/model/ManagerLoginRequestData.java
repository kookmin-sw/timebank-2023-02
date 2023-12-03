package kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model;

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
        @JsonSubTypes.Type(value = ManagerLoginRequestData.PasswordLoginRequestData.class, name = "password"),
        @JsonSubTypes.Type(value = ManagerLoginRequestData.SocialLoginRequestData.class, name = "social")
})
public abstract class ManagerLoginRequestData {

    private final AuthenticationType authenticationType;

    public ManagerLoginRequestData(AuthenticationType authenticationType) {
        this.authenticationType = authenticationType;
    }

    public abstract AuthenticationRequest toAuthenticationRequest();

    @JsonTypeName("social")
    public static final class SocialLoginRequestData extends ManagerLoginRequestData {

        private final SocialPlatformType socialPlatformType;

        @NotBlank(message = "액세스 토큰은 필수입니다.")
        private final String accessToken;

        public SocialLoginRequestData(
                SocialPlatformType socialPlatformType,
                @NotBlank(message = "액세스 토큰은 필수입니다.") String accessToken
        ) {
            super(AuthenticationType.SOCIAL);
            this.socialPlatformType = socialPlatformType;
            this.accessToken = accessToken;
        }

        @Override
        public AuthenticationRequest toAuthenticationRequest() {
            return new SocialAuthenticationRequest(
                    AccountType.BRANCH,
                    socialPlatformType,
                    accessToken
            );
        }
    }

    @JsonTypeName("password")
    public static final class PasswordLoginRequestData extends ManagerLoginRequestData {

        @NotBlank(message = "아이디는 필수입니다.")
        private final String username;

        @NotBlank(message = "비밀번호는 필수입니다.")
        private final String password;

        public PasswordLoginRequestData(
                @NotBlank(message = "아이디는 필수입니다.") String username,
                @NotBlank(message = "비밀번호는 필수입니다.") String password
        ) {
            super(AuthenticationType.PASSWORD);
            this.username = username;
            this.password = password;
        }

        @Override
        public AuthenticationRequest toAuthenticationRequest() {
            return new PasswordAuthenticationRequest(
                    AccountType.BRANCH,
                    username,
                    password
            );
        }
    }
}
