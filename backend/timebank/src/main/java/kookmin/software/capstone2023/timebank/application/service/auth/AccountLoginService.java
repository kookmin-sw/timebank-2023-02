package kookmin.software.capstone2023.timebank.application.service.auth;

import kookmin.software.capstone2023.timebank.application.exception.UnauthorizedException;
import kookmin.software.capstone2023.timebank.application.service.auth.model.AuthenticationRequest;
import kookmin.software.capstone2023.timebank.application.service.auth.model.PasswordAuthenticationRequest;
import kookmin.software.capstone2023.timebank.application.service.auth.model.SocialAuthenticationRequest;
import kookmin.software.capstone2023.timebank.application.service.auth.token.AccessTokenService;
import kookmin.software.capstone2023.timebank.domain.model.Account;
import kookmin.software.capstone2023.timebank.domain.model.User;
import kookmin.software.capstone2023.timebank.domain.model.auth.PasswordAuthentication;
import kookmin.software.capstone2023.timebank.domain.model.auth.SocialAuthentication;
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.PasswordAuthenticationJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.SocialAuthenticationJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.UserJpaRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Data
@AllArgsConstructor
public class AccountLoginService {
    private final SocialPlatformUserFindService socialPlatformUserFindService;
    private final AccessTokenService accessTokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserJpaRepository userJpaRepository;
    private final AccountJpaRepository accountJpaRepository;
    private final SocialAuthenticationJpaRepository socialAuthenticationJpaRepository;
    private final PasswordAuthenticationJpaRepository passwordAuthenticationJpaRepository;

    @Data
    public static class TokenData {
        private final String accessToken;
        private final Instant expiresAt;
    }

    @Transactional
    public TokenData login(AuthenticationRequest authenticationRequest) {
        long userId = authenticate(authenticationRequest);

        User user = userJpaRepository.findById(userId).get();
        if (user == null) {
            throw new UnauthorizedException("등록되지 않은 사용자입니다.");
        }

        Account account = accountJpaRepository.findById(user.getAccount().getId()).get();
        if (account == null) {
            throw new UnauthorizedException("등록되지 않은 사용자입니다.");
        }

        if (authenticationRequest.getAccountType() != null && account.getType() != authenticationRequest.getAccountType()) {
            throw new UnauthorizedException("권한이 없습니다.");
        }

        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);

        String accessToken = accessTokenService.issue(user.getId(), user.getAccount().getId(), expiresAt);

        user.updateLastLoginAt(LocalDateTime.now());

        return new TokenData(accessToken, expiresAt);
    }

    public long authenticate(AuthenticationRequest authenticationRequest) {
        if (authenticationRequest instanceof SocialAuthenticationRequest) {
            SocialAuthenticationRequest socialAuthenticationRequest = (SocialAuthenticationRequest) authenticationRequest;
            SocialPlatformUserFindService.SocialUser socialUser = socialPlatformUserFindService.getUser(socialAuthenticationRequest.getSocialPlatformType(), socialAuthenticationRequest.getAccessToken());
            SocialAuthentication socialAuthentication = socialAuthenticationJpaRepository.findByPlatformTypeAndPlatformUserId(socialAuthenticationRequest.getSocialPlatformType(), socialUser.getId());
            if (socialAuthentication == null) {
                throw new UnauthorizedException("등록되지 않은 사용자입니다.");
            }
            return socialAuthentication.getUserId();
        } else if (authenticationRequest instanceof PasswordAuthenticationRequest) {
            PasswordAuthenticationRequest passwordAuthenticationRequest = (PasswordAuthenticationRequest) authenticationRequest;
            PasswordAuthentication authentication = passwordAuthenticationJpaRepository.findByUsername(passwordAuthenticationRequest.getUsername());
            if (authentication == null) {
                throw new UnauthorizedException("등록되지 않은 사용자입니다.");
            }
            if (!passwordEncoder.matches(passwordAuthenticationRequest.getPassword(), authentication.getPassword())) {
                throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
            }
            return authentication.getUserId();
        }
        throw new UnauthorizedException("인증 방법이 지원되지 않습니다.");
    }
}
