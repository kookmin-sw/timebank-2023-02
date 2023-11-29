package kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ManagerPaymentRequestData {

    @NotBlank(message = "지점 계좌 번호는 필수입니다.")
    private final String branchBankAccountNumber;

    @NotBlank(message = "사용자 계좌 번호는 필수입니다.")
    private final String userBankAccountNumber;

    @NotNull(message = "이체여부는 필수입니다")
    private final Boolean isDeposit;

    @NotNull(message = "거래 금액은 필수입니다")
    private final BigDecimal amount;

    public ManagerPaymentRequestData(String branchBankAccountNumber, String userBankAccountNumber, Boolean isDeposit, BigDecimal amount) {
        this.branchBankAccountNumber = branchBankAccountNumber;
        this.userBankAccountNumber = userBankAccountNumber;
        this.isDeposit = isDeposit;
        this.amount = amount;
    }

    public String getBranchBankAccountNumber() {
        return branchBankAccountNumber;
    }

    public String getUserBankAccountNumber() {
        return userBankAccountNumber;
    }

    public Boolean getDeposit() {
        return isDeposit;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
