package kookmin.software.capstone2023.timebank.application.service.auth;

import kookmin.software.capstone2023.timebank.domain.model.AccountType;

public class TestEnvironmentUserAuthenticator implements UserAuthenticator {
    private final long userId;
    private final long accountId;
    private final AccountType accountType;

    public TestEnvironmentUserAuthenticator(long userId, long accountId, AccountType accountType) {
        this.userId = userId;
        this.accountId = accountId;
        this.accountType = accountType;
    }

    @Override
    public UserAuthenticator.AuthenticationData authenticate(String accessToken) {
        return new UserAuthenticator.AuthenticationData(userId, accountId, accountType);
    }
}
