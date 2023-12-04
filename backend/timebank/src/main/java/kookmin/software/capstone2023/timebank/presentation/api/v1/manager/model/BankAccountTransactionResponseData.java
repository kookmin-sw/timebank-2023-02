package kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model;

import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction;
import kookmin.software.capstone2023.timebank.domain.model.TransactionCode;
import kookmin.software.capstone2023.timebank.domain.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankAccountTransactionResponseData {
    private final Long id;
    private final TransactionCode code;
    private final BigDecimal amount;
    private final TransactionStatus status;
    private final Long senderAccountId;
    private final String senderBankAccountNumber;
    private final String senderAccountOwnerName;
    private final Long receiverAccountId;
    private final String receiverBankAccountNumber;
    private final String receiverAccountOwnerName;
    private final BigDecimal balanceSnapshot;
    private final LocalDateTime transactionAt;

    public BankAccountTransactionResponseData(Long id, TransactionCode code, BigDecimal amount, TransactionStatus status,
                                              Long senderAccountId, String senderBankAccountNumber, String senderAccountOwnerName,
                                              Long receiverAccountId, String receiverBankAccountNumber, String receiverAccountOwnerName,
                                              BigDecimal balanceSnapshot, LocalDateTime transactionAt) {
        this.id = id;
        this.code = code;
        this.amount = amount;
        this.status = status;
        this.senderAccountId = senderAccountId;
        this.senderBankAccountNumber = senderBankAccountNumber;
        this.senderAccountOwnerName = senderAccountOwnerName;
        this.receiverAccountId = receiverAccountId;
        this.receiverBankAccountNumber = receiverBankAccountNumber;
        this.receiverAccountOwnerName = receiverAccountOwnerName;
        this.balanceSnapshot = balanceSnapshot;
        this.transactionAt = transactionAt;
    }

    public Long getId() {
        return id;
    }

    public TransactionCode getCode() {
        return code;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Long getSenderAccountId() {
        return senderAccountId;
    }

    public String getSenderBankAccountNumber() {
        return senderBankAccountNumber;
    }

    public String getSenderAccountOwnerName() {
        return senderAccountOwnerName;
    }

    public Long getReceiverAccountId() {
        return receiverAccountId;
    }

    public String getReceiverBankAccountNumber() {
        return receiverBankAccountNumber;
    }

    public String getReceiverAccountOwnerName() {
        return receiverAccountOwnerName;
    }

    public BigDecimal getBalanceSnapshot() {
        return balanceSnapshot;
    }

    public LocalDateTime getTransactionAt() {
        return transactionAt;
    }

    public static BankAccountTransactionResponseData fromDomain(BankAccountTransaction transaction) {
        return new BankAccountTransactionResponseData(
                transaction.getId(),
                transaction.getCode(),
                transaction.getAmount(),
                transaction.getStatus(),
                transaction.getSenderBankAccount().getId(),
                transaction.getSenderBankAccount().getAccountNumber(),
                transaction.getSenderBankAccount().getOwnerName(),
                transaction.getReceiverBankAccount().getId(),
                transaction.getReceiverBankAccount().getAccountNumber(),
                transaction.getReceiverBankAccount().getOwnerName(),
                transaction.getBalanceSnapshot(),
                transaction.getTransactionAt()
        );
    }
}
