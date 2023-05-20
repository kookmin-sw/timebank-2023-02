package kookmin.software.capstone2023.timebank.presentation.api.v1

import kookmin.software.capstone2023.timebank.application.service.bank.transfer.TransferService
import kookmin.software.capstone2023.timebank.application.service.bank.transfer.TransferServiceImpl
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext
import kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.transfer.BankAccountTransferRequestData
import kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.transfer.BankFundTransferResponseData
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@UserAuthentication
@RestController
@RequestMapping("api/v1/bank/account/transfer")
class BankTransferController(
    private val transferService: TransferServiceImpl,
) {
    @PostMapping
    fun transfer(
        @RequestAttribute(RequestAttributes.USER_CONTEXT) userContext: UserContext,
        @Validated @RequestBody
        data: BankAccountTransferRequestData,
    ): BankFundTransferResponseData {
        val response = transferService.transfer(
            TransferService.TransferRequest(
                accountId = userContext.accountId,
                senderAccountNumber = data.senderBankAccountNumber,
                receiverAccountNumber = data.receiverBankAccountNumber,
                amount = data.amount,
                password = data.password,
            ),
        )
        return BankFundTransferResponseData(
            transactionAt = response.transactionAt,
            amount = response.amount,
            balanceSnapshot = response.balanceSnapshot,
            status = response.status,
            senderBankAccountNumber = response.senderBankAccount.accountNumber,
            receiverBankAccountNumber = response.receiverBankAccount.accountNumber,
        )
    }
}
