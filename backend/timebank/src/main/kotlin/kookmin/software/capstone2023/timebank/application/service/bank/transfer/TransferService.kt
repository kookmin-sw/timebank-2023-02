package kookmin.software.capstone2023.timebank.application.service.bank.transfer

import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction
import java.math.BigDecimal

interface TransferService {
    fun transfer(request: TransferRequest): BankAccountTransaction
    data class TransferRequest(
        val senderAccountNumber: String,
        val receiverAccountNumber: String,
        val amount: BigDecimal,
        val password: String? = null,
    )
}
