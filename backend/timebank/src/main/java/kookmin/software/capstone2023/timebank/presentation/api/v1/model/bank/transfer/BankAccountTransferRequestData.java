package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.transfer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BankAccountTransferRequestData {

    @NotBlank(message = "보낸이 계좌번호는 필수입니다.")
    private final String senderBankAccountNumber;

    @NotBlank(message = "받은이 계좌번호는 필수입니다.")
    private final String receiverBankAccountNumber;

    // 필수 입력
    @NotNull(message = "거래 금액은 필수입니다")
    private final BigDecimal amount;

    @NotBlank(message = "사용자 계좌 비밀번호는 필수입니다.")
    private final String password;

    public BankAccountTransferRequestData(String senderBankAccountNumber,
                                          String receiverBankAccountNumber,
                                          BigDecimal amount,
                                          String password) {
        this.senderBankAccountNumber = senderBankAccountNumber;
        this.receiverBankAccountNumber = receiverBankAccountNumber;
        this.amount = amount;
        this.password = password;
    }

    public String getSenderBankAccountNumber() {
        return senderBankAccountNumber;
    }

    public String getReceiverBankAccountNumber() {
        return receiverBankAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getPassword() {
        return password;
    }
}
