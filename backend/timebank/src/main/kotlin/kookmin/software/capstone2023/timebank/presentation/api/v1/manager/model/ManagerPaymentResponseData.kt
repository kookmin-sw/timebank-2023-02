package kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model

import kookmin.software.capstone2023.timebank.domain.model.TransactionCode
import kookmin.software.capstone2023.timebank.domain.model.TransactionStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class ManagerPaymentResponseData(

    val branchBankAccountNumber: String, // 은행 지점 계좌

    val userBankAccountNumber: String, // 은행 계좌

    val transactionCode: TransactionCode, // WITHDRAW 출금, DEPOSIT 이체

    val transactionStatus: TransactionStatus, // 거래 상태

    val amount: BigDecimal, // 거래 금액

    val updatedAt: LocalDateTime, // 거래 시간
)
