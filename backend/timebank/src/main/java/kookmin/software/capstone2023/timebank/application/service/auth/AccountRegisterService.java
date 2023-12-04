package kookmin.software.capstone2023.timebank.application.service.auth;

import kookmin.software.capstone2023.timebank.application.exception.ConflictException;
import kookmin.software.capstone2023.timebank.application.service.auth.model.AuthenticationRequest;
import kookmin.software.capstone2023.timebank.application.service.auth.model.PasswordAuthenticationRequest;
import kookmin.software.capstone2023.timebank.application.service.auth.model.SocialAuthenticationRequest;
import kookmin.software.capstone2023.timebank.domain.model.Account;
import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.domain.model.Gender;
import kookmin.software.capstone2023.timebank.domain.model.User;
import kookmin.software.capstone2023.timebank.domain.model.auth.PasswordAuthentication;
import kookmin.software.capstone2023.timebank.domain.model.auth.SocialAuthentication;
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.PasswordAuthenticationJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.SocialAuthenticationJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.UserJpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
public class AccountRegisterService {

    private final SocialPlatformUserFindService socialPlatformUserFindService;
    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final AccountJpaRepository accountJpaRepository;
    private final SocialAuthenticationJpaRepository socialAuthenticationJpaRepository;
    private final PasswordAuthenticationJpaRepository passwordAuthenticationJpaRepository;

    public AccountRegisterService(
            SocialPlatformUserFindService socialPlatformUserFindService,
            PasswordEncoder passwordEncoder,
            UserJpaRepository userJpaRepository,
            AccountJpaRepository accountJpaRepository,
            SocialAuthenticationJpaRepository socialAuthenticationJpaRepository,
            PasswordAuthenticationJpaRepository passwordAuthenticationJpaRepository) {
        this.socialPlatformUserFindService = socialPlatformUserFindService;
        this.passwordEncoder = passwordEncoder;
        this.userJpaRepository = userJpaRepository;
        this.accountJpaRepository = accountJpaRepository;
        this.socialAuthenticationJpaRepository = socialAuthenticationJpaRepository;
        this.passwordAuthenticationJpaRepository = passwordAuthenticationJpaRepository;
    }

    @Transactional
    public void register(
            AuthenticationRequest authentication,
            String name,
            String phoneNumber,
            Gender gender,
            LocalDate birthday,
            AccountType accountType) {
        validateDuplicatedRegistration(authentication);

        Account account = accountJpaRepository.save(
                new Account(accountType, null, name)
        );

        User user = userJpaRepository.save(
                new User(authentication.getType(), account, name, phoneNumber, gender, birthday, null)
        );

        if (authentication instanceof SocialAuthenticationRequest) {
            SocialAuthenticationRequest socialAuthRequest = (SocialAuthenticationRequest) authentication;
            SocialPlatformUserFindService.SocialUser socialUser = socialPlatformUserFindService.getUser(socialAuthRequest.getSocialPlatformType(), socialAuthRequest.getAccessToken());
            socialAuthenticationJpaRepository.save(
                    new SocialAuthentication(user.getId(), socialAuthRequest.getSocialPlatformType(), socialUser.getId())
            );
        } else if (authentication instanceof PasswordAuthenticationRequest) {
            PasswordAuthenticationRequest passwordAuthRequest = (PasswordAuthenticationRequest) authentication;
            String encodedPassword = passwordEncoder.encode(passwordAuthRequest.getPassword());
            passwordAuthenticationJpaRepository.save(
                    new PasswordAuthentication(user.getId(), passwordAuthRequest.getUsername(), encodedPassword)
            );
        }
    }

    private void validateDuplicatedRegistration(AuthenticationRequest authentication) {
        if (authentication instanceof SocialAuthenticationRequest) {
            SocialAuthenticationRequest socialAuthRequest = (SocialAuthenticationRequest) authentication;
            SocialPlatformUserFindService.SocialUser socialUser = socialPlatformUserFindService.getUser(socialAuthRequest.getSocialPlatformType(), socialAuthRequest.getAccessToken());
            SocialAuthentication socialAuthentication = socialAuthenticationJpaRepository.findByPlatformTypeAndPlatformUserId(socialAuthRequest.getSocialPlatformType(), socialUser.getId());
            if (socialAuthentication != null) {
                throw new ConflictException("이미 등록된 사용자입니다.");
            }
        } else if (authentication instanceof PasswordAuthenticationRequest) {
            PasswordAuthenticationRequest passwordAuthRequest = (PasswordAuthenticationRequest) authentication;
            boolean existsAuthentication = passwordAuthenticationJpaRepository.existsByUsername(passwordAuthRequest.getUsername());
            if (existsAuthentication) {
                throw new ConflictException("이미 등록된 사용자입니다.");
            }
        }
    }
}
