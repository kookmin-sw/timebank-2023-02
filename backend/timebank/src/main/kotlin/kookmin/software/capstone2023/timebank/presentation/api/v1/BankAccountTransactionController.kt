package kookmin.software.capstone2023.timebank.presentation.api.v1

import kookmin.software.capstone2023.timebank.application.service.bank.account.BankAccountReadService
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountTransactionJpaRepository
import kookmin.software.capstone2023.timebank.domain.repository.spec.BankAccountTransactionSpecs
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.BankAccountTransactionResponseData
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@UserAuthentication
@RequestMapping("/api/v1/bank/account/transaction")
@RestController
class BankAccountTransactionController(
    private val bankAccountReadService: BankAccountReadService,
    private val bankAccountTransactionJpaRepository: BankAccountTransactionJpaRepository,
) {
    @GetMapping("/{bankAccountNumber}")
    fun getBankAccountTransactionsByBankAccountNumber(
        @RequestAttribute(RequestAttributes.USER_CONTEXT) userContext: UserContext,
        @PathVariable bankAccountNumber: String,
        @PageableDefault pageable: Pageable,
    ): Page<BankAccountTransactionResponseData> {
        val bankAccount = bankAccountReadService.getBankAccountByBankAccountNumber(bankAccountNumber)
        return bankAccountTransactionJpaRepository.findAll(
            Specification.allOf(
                BankAccountTransactionSpecs.withBankAccountId(bankAccount.id),
            ),
            pageable = pageable,
        ).map {
            BankAccountTransactionResponseData.fromDomain(it)
        }
    }
}
