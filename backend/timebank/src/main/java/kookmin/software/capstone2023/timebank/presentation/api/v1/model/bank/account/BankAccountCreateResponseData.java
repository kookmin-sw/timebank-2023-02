package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.account;

import java.math.BigDecimal;

public class BankAccountCreateResponseData {

    private final BigDecimal balance;
    private final String accountNumber;
    private final Long bankAccountId;

    public BankAccountCreateResponseData(BigDecimal balance, String accountNumber, Long bankAccountId) {
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.bankAccountId = bankAccountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }
}
