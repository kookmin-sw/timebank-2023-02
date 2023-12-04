package kookmin.software.capstone2023.timebank.presentation.api.auth.model;

import kookmin.software.capstone2023.timebank.domain.model.AccountType;

public class UserContext {
    private final long userId;
    private final long accountId;
    private final AccountType accountType;

    public UserContext(long userId, long accountId, AccountType accountType) {
        this.userId = userId;
        this.accountId = accountId;
        this.accountType = accountType;
    }

    public long getUserId() {
        return userId;
    }

    public long getAccountId() {
        return accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
