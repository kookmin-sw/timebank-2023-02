package kookmin.software.capstone2023.timebank.application.service.bank.transfer;

import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction;
import lombok.Data;

import java.math.BigDecimal;

public interface TransferService {

    BankAccountTransaction transfer(TransferRequest request);

    @Data
    class TransferRequest {
        private final Long accountId;
        private final String senderAccountNumber;
        private final String receiverAccountNumber;
        private final BigDecimal amount;
        private final String password;

        public TransferRequest(Long accountId, String senderAccountNumber, String receiverAccountNumber, BigDecimal amount, String password) {
            this.accountId = accountId;
            this.senderAccountNumber = senderAccountNumber;
            this.receiverAccountNumber = receiverAccountNumber;
            this.amount = amount;
            this.password = password;
        }

        public TransferRequest(Long accountId, String senderAccountNumber, String receiverAccountNumber, BigDecimal amount) {
            this.accountId = accountId;
            this.senderAccountNumber = senderAccountNumber;
            this.receiverAccountNumber = receiverAccountNumber;
            this.amount = amount;
            this.password = null;
        }
    }
}
