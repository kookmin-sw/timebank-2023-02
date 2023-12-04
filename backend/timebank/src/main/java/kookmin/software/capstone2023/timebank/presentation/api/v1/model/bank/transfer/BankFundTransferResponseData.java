package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.transfer;

import kookmin.software.capstone2023.timebank.domain.model.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankFundTransferResponseData {

    private final LocalDateTime transactionAt;
    private final BigDecimal amount;
    private final BigDecimal balanceSnapshot;
    private final TransactionStatus status;
    private final String senderBankAccountNumber;
    private final String receiverBankAccountNumber;

    public BankFundTransferResponseData(LocalDateTime transactionAt,
                                        BigDecimal amount,
                                        BigDecimal balanceSnapshot,
                                        TransactionStatus status,
                                        String senderBankAccountNumber,
                                        String receiverBankAccountNumber) {
        this.transactionAt = transactionAt;
        this.amount = amount;
        this.balanceSnapshot = balanceSnapshot;
        this.status = status;
        this.senderBankAccountNumber = senderBankAccountNumber;
        this.receiverBankAccountNumber = receiverBankAccountNumber;
    }

    public LocalDateTime getTransactionAt() {
        return transactionAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getBalanceSnapshot() {
        return balanceSnapshot;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getSenderBankAccountNumber() {
        return senderBankAccountNumber;
    }

    public String getReceiverBankAccountNumber() {
        return receiverBankAccountNumber;
    }
}
