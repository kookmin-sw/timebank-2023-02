package kookmin.software.capstone2023.timebank.application.configuration;

import kookmin.software.capstone2023.timebank.domain.model.AccountType;


public class UserProperties {
    private long id;
    private long accountId;
    private AccountType accountType;

    public long getId() { return id; }
    public long getAccountId() { return accountId; }
    public AccountType getAccountType() { return accountType; }

    public void setId(long id) {
        this.id = id;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
