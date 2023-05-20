package kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class ManagerPaymentRequestData(

    @field:NotBlank(message = "지점 계좌 번호는 필수입니다.")
    val branchBankAccountNumber: String, // 은행 지점 계좌

    @field:NotBlank(message = "사용자 계좌 번호는 필수입니다.")
    val userBankAccountNumber: String, // 은행 계좌

    @field:NotNull(message = "이체여부는 필수입니다")
    val isDeposit: Boolean, // WITHDRAW 출금, DEPOSIT 이체

    // 필수 입력
    @field:NotNull(message = "거래 금액은 필수입니다")
    val amount: BigDecimal, // 거래 금액
)
