package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.account;

import java.time.LocalDateTime;

public class PasswordUpdateResponseData {

    private final String bankAccountNumber; // 계좌 번호
    private final LocalDateTime updateAt; // 변경 일자
    private final String resResultCode; // 결과 코드

    public PasswordUpdateResponseData(String bankAccountNumber, LocalDateTime updateAt, String resResultCode) {
        this.bankAccountNumber = bankAccountNumber;
        this.updateAt = updateAt;
        this.resResultCode = resResultCode;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public String getResResultCode() {
        return resResultCode;
    }
}
