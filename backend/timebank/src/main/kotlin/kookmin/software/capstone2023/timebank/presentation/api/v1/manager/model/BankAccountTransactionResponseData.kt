package kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model

import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction
import kookmin.software.capstone2023.timebank.domain.model.TransactionCode
import kookmin.software.capstone2023.timebank.domain.model.TransactionStatus
import java.math.BigDecimal
import java.time.LocalDateTime

data class BankAccountTransactionResponseData(
    val id: Long,
    val code: TransactionCode,
    val amount: BigDecimal,
    val status: TransactionStatus,
    val senderAccountId: Long,
    val senderBankAccountNumber: String,
    val senderAccountOwnerName: String,
    val receiverAccountId: Long,
    val receiverBankAccountNumber: String,
    val receiverAccountOwnerName: String,
    val balanceSnapshot: BigDecimal,
    val transactionAt: LocalDateTime,
) {
    companion object {
        fun fromDomain(transaction: BankAccountTransaction): BankAccountTransactionResponseData {
            return BankAccountTransactionResponseData(
                id = transaction.id,
                code = transaction.code,
                amount = transaction.amount,
                status = transaction.status,
                senderAccountId = transaction.senderBankAccount.id,
                senderBankAccountNumber = transaction.senderBankAccount.accountNumber,
                senderAccountOwnerName = transaction.senderBankAccount.ownerName,
                receiverAccountId = transaction.receiverBankAccount.id,
                receiverBankAccountNumber = transaction.receiverBankAccount.accountNumber,
                receiverAccountOwnerName = transaction.receiverBankAccount.ownerName,
                balanceSnapshot = transaction.balanceSnapshot,
                transactionAt = transaction.transactionAt,
            )
        }
    }
}
