package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.account;

import jakarta.validation.constraints.NotBlank;

public class BankAccountCreateRequestData {

    @NotBlank(message = "생성하려는 은행 계정의 패스워드를 보내주세요")
    private final String password;

    public BankAccountCreateRequestData(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
