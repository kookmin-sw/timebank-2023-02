package kookmin.software.capstone2023.timebank.application.service.auth;

import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

public interface UserAuthenticator {
    @Data
    @RequiredArgsConstructor
    class AuthenticationData {
        private final long userId;
        private final long accountId;
        private final AccountType accountType;
    }

    AuthenticationData authenticate(String accessToken);
}
