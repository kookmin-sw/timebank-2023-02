package kookmin.software.capstone2023.timebank.presentation.api.v1.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import kookmin.software.capstone2023.timebank.application.service.auth.model.AuthenticationRequest;
import kookmin.software.capstone2023.timebank.application.service.auth.model.PasswordAuthenticationRequest;
import kookmin.software.capstone2023.timebank.application.service.auth.model.SocialAuthenticationRequest;
import kookmin.software.capstone2023.timebank.domain.model.Gender;
import kookmin.software.capstone2023.timebank.domain.model.auth.AuthenticationType;
import kookmin.software.capstone2023.timebank.domain.model.auth.SocialPlatformType;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "authenticationType"
)
public abstract class UserRegisterRequestData {

    @JsonTypeName("social")
    public static class SocialUserRegisterRequestData extends UserRegisterRequestData {

        private final SocialPlatformType socialPlatformType;
        private final String accessToken;

        public SocialUserRegisterRequestData(
                SocialPlatformType socialPlatformType,
                @NotBlank(message = "액세스 토큰은 필수입니다.")
                String accessToken,
                @NotBlank(message = "이름을 입력해주세요.")
                @Length(max = 20, message = "이름은 20자 이하로 입력해주세요.")
                String name,
                @NotBlank(message = "전화번호를 입력해주세요.")
                @Pattern(regexp = "^[0-9]{11}$", message = "전화번호는 11자리 숫자만 입력 가능합니다.")
                String phoneNumber,
                Gender gender,
                LocalDate birthday
        ) {
            super(AuthenticationType.SOCIAL, name, phoneNumber, gender, birthday);
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
                    null, socialPlatformType, accessToken);
        }
    }

    @JsonTypeName("password")
    public static class UserPasswordRegisterRequestData extends UserRegisterRequestData {

        private final String username;
        private final String password;

        public UserPasswordRegisterRequestData(
                @NotBlank(message = "아이디는 필수입니다.")
                String username,
                @NotBlank(message = "비밀번호는 필수입니다.")
                String password,
                @NotBlank(message = "이름을 입력해주세요.")
                @Length(max = 20, message = "이름은 20자 이하로 입력해주세요.")
                String name,
                @NotBlank(message = "전화번호를 입력해주세요.")
                @Pattern(regexp = "^[0-9]{11}$", message = "전화번호는 11자리 숫자만 입력 가능합니다.")
                String phoneNumber,
                Gender gender,
                LocalDate birthday
        ) {
            super(AuthenticationType.PASSWORD, name, phoneNumber, gender, birthday);
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
                    null, username, password);
        }
    }

    private final AuthenticationType authenticationType;
    private final String name;
    private final String phoneNumber;
    private final Gender gender;
    private final LocalDate birthday;

    protected UserRegisterRequestData(
            AuthenticationType authenticationType,
            String name,
            String phoneNumber,
            Gender gender,
            LocalDate birthday
    ) {
        this.authenticationType = authenticationType;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthday = birthday;
    }

    public AuthenticationType getAuthenticationType() {
        return authenticationType;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public abstract AuthenticationRequest toAuthenticationRequest();
}
