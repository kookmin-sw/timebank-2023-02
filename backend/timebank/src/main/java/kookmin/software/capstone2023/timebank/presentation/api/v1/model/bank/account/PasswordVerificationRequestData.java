package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.account;

import jakarta.validation.constraints.NotBlank;

public class PasswordVerificationRequestData {

    @NotBlank(message = "은행 계좌 번호를 보내주세요")
    private final String bankAccountNumber;

    @NotBlank(message = "은행 계좌 패스워드를 보내주세요")
    private final String password;

    public PasswordVerificationRequestData(String bankAccountNumber, String password) {
        this.bankAccountNumber = bankAccountNumber;
        this.password = password;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getPassword() {
        return password;
    }
}
