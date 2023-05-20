package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.transfer

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class BankAccountTransferRequestData(

    @field:NotBlank(message = "보낸이 계좌번호는 필수입니다.")
    val senderBankAccountNumber: String,

    @field:NotBlank(message = "받은이 계좌번호는 필수입니다.")
    val receiverBankAccountNumber: String,

    // 필수 입력
    @field:NotNull(message = "거래 금액은 필수입니다")
    val amount: BigDecimal,

    @field:NotBlank(message = "사용자 계좌 비밀번호는 필수입니다.")
    val password: String,
)
