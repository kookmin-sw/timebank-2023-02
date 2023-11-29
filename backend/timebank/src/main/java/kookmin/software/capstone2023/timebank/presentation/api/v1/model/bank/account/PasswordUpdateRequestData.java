package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.account;

public class PasswordUpdateRequestData {

    private final String beforePassword; // 이전 비밀번호
    private final String afterPassword; // 변경할 비밀번호
    private final String bankAccountNumber; // 계좌 번호

    public PasswordUpdateRequestData(String beforePassword, String afterPassword, String bankAccountNumber) {
        this.beforePassword = beforePassword;
        this.afterPassword = afterPassword;
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBeforePassword() {
        return beforePassword;
    }

    public String getAfterPassword() {
        return afterPassword;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }
}
