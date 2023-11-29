package kookmin.software.capstone2023.timebank.application.configuration;

import kookmin.software.capstone2023.timebank.application.service.auth.DefaultUserAuthenticator;
import kookmin.software.capstone2023.timebank.application.service.auth.TestEnvironmentUserAuthenticator;
import kookmin.software.capstone2023.timebank.application.service.auth.UserAuthenticator;
import kookmin.software.capstone2023.timebank.application.service.auth.token.AccessTokenService;
import kookmin.software.capstone2023.timebank.core.LoggerExtensions;
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.UserJpaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.slf4j.Logger;

@Configuration
public class UserAuthenticatorConfiguration {

    private final AuthenticationTestProperties authenticationTestProperties;
    private final Logger logger;

    public UserAuthenticatorConfiguration(AuthenticationTestProperties authenticationTestProperties) {
        this.authenticationTestProperties = authenticationTestProperties;
        this.logger = LoggerExtensions.logger(UserAuthenticatorConfiguration.class);
    }

    @Bean
    @Profile("dev")
    public UserAuthenticator devEnvironmentUserAuthenticator(
            AccessTokenService accessTokenService,
            UserJpaRepository userJpaRepository,
            AccountJpaRepository accountJpaRepository
    ) {
        // 인증 테스트가 활성화되어 있으면 테스트 환경 사용자 인증기를 반환한다.
        if (authenticationTestProperties.getEnabled() != null && authenticationTestProperties.getEnabled()) {
            UserProperties user = authenticationTestProperties.getUser();
            if (user == null) {
                throw new IllegalStateException("Test environment user is not configured.");
            }

            logger.info("Test environment user authenticator is enabled.");
            logger.info("Test environment user: " + user);

            return new TestEnvironmentUserAuthenticator(
                    user.getId(),
                    user.getAccountId(),
                    user.getAccountType()
            );
        }

        logger.info("Test environment user authenticator is disabled. (default user authenticator is enabled.");

        return new DefaultUserAuthenticator(
                accessTokenService,
                userJpaRepository,
                accountJpaRepository
        );
    }

    @Bean
    @Profile("!dev")
    public UserAuthenticator defaultUserAuthenticator(
            AccessTokenService accessTokenService,
            UserJpaRepository userJpaRepository,
            AccountJpaRepository accountJpaRepository
    ) {
        return new DefaultUserAuthenticator(
                accessTokenService,
                userJpaRepository,
                accountJpaRepository
        );
    }
}
