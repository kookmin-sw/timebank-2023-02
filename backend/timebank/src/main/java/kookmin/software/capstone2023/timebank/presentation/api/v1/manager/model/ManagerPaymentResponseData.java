package kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model;

import kookmin.software.capstone2023.timebank.domain.model.TransactionCode;
import kookmin.software.capstone2023.timebank.domain.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ManagerPaymentResponseData {

    private final String branchBankAccountNumber;
    private final String userBankAccountNumber;
    private final TransactionCode transactionCode;
    private final TransactionStatus transactionStatus;
    private final BigDecimal amount;
    private final LocalDateTime updatedAt;

    public ManagerPaymentResponseData(String branchBankAccountNumber, String userBankAccountNumber,
                                      TransactionCode transactionCode, TransactionStatus transactionStatus,
                                      BigDecimal amount, LocalDateTime updatedAt) {
        this.branchBankAccountNumber = branchBankAccountNumber;
        this.userBankAccountNumber = userBankAccountNumber;
        this.transactionCode = transactionCode;
        this.transactionStatus = transactionStatus;
        this.amount = amount;
        this.updatedAt = updatedAt;
    }

    public String getBranchBankAccountNumber() {
        return branchBankAccountNumber;
    }

    public String getUserBankAccountNumber() {
        return userBankAccountNumber;
    }

    public TransactionCode getTransactionCode() {
        return transactionCode;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
