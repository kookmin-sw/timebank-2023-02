package kookmin.software.capstone2023.timebank.application.service.account

import kookmin.software.capstone2023.timebank.application.exception.NotFoundException
import kookmin.software.capstone2023.timebank.domain.model.Account
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository
import org.springframework.data.repository.findByIdOrNull

class AccountReadService(
    private val accountRepository: AccountJpaRepository,
) {
    fun getAccountById(
        accountId: Long,
    ): Account {
        return accountRepository.findByIdOrNull(accountId)
            ?: throw NotFoundException(message = "Account not found")
    }
}
