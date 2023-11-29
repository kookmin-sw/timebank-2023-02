package kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model;

import kookmin.software.capstone2023.timebank.domain.model.BankAccount;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class BankAccountResponseData {
    private final Long id;
    private final Long branchId;
    private final String branchName;
    private final Long accountId;
    private final String accountName;
    private final String accountNumber;
    private final BigDecimal balanceAmount;
    private final ZonedDateTime createdAt;

    public BankAccountResponseData(Long id, Long branchId, String branchName, Long accountId, String accountName,
                                   String accountNumber, BigDecimal balanceAmount, ZonedDateTime createdAt) {
        this.id = id;
        this.branchId = branchId;
        this.branchName = branchName;
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.balanceAmount = balanceAmount;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getBranchId() {
        return branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public Long getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public static BankAccountResponseData fromDomain(BankAccount bankAccount) {
        return new BankAccountResponseData(
                bankAccount.getId(),
                bankAccount.getBranch().getId(),
                bankAccount.getBranch().getName(),
                bankAccount.getAccount().getId(),
                bankAccount.getAccount().getName(),
                bankAccount.getAccountNumber(),
                bankAccount.getBalance(),
                bankAccount.getCreatedAt().atZone(ZoneId.of("Asia/Seoul"))
        );
    }
}
