package kookmin.software.capstone2023.timebank.application.service.bank.account

import kookmin.software.capstone2023.timebank.application.exception.ConflictException
import kookmin.software.capstone2023.timebank.application.exception.NotFoundException
import kookmin.software.capstone2023.timebank.domain.model.Account
import kookmin.software.capstone2023.timebank.domain.model.AccountType
import kookmin.software.capstone2023.timebank.domain.model.BankAccount
import kookmin.software.capstone2023.timebank.domain.model.BankBranch
import kookmin.software.capstone2023.timebank.domain.model.OwnerType
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountJpaRepository
import kookmin.software.capstone2023.timebank.domain.repository.BankBranchJpaRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class BankAccountCreateService(
    private val bankAcoountReadService: BankAccountReadService,
    private val bankAccountRepository: BankAccountJpaRepository,
    private val bankBranchJpaRepository: BankBranchJpaRepository,
    private val AccountRepository: AccountJpaRepository,
) {
    @Transactional
    fun createBankAccount(
        accountId: Long,
        password: String,
        branchId: Long,
    ): CreatedBankAccount {
        val account: Account = AccountRepository.findByIdOrNull(accountId)
            ?: throw NotFoundException(message = "계정을 찾을 수 없습니다.")

        val branch: BankBranch = bankBranchJpaRepository.findByIdOrNull(branchId)
            ?: throw NotFoundException(message = "지점이 존재하지 않습니다.")

        val bankAccount = BankAccount(
            account = account,
            branch = branch,
            balance = BigDecimal(300.0),
            ownerName = account.name,
            ownerType = if (account.type == AccountType.INDIVIDUAL) {
                OwnerType.USER
            } else {
                OwnerType.BRANCH
            },
            accountNumber = generateAccountNumber(accountId, branchId),
            password = password,
        )

        // 계좌 생성
        val createdBankAccount = bankAccountRepository.save(bankAccount)

        return CreatedBankAccount(
            bankAccountId = createdBankAccount.id,
            branchId = createdBankAccount.branch.id,
            balance = createdBankAccount.balance,
            ownerName = createdBankAccount.ownerName,
            ownerType = createdBankAccount.ownerType,
            bankAccountNumber = createdBankAccount.accountNumber,
            createdAt = createdBankAccount.createdAt,
        )
    }

    private fun generateAccountNumber(accountId: Long, branchId: Long): String {
        val accountCode = accountId.toString().padStart(2, '0')
        val branchCode = branchId.toString().padStart(2, '0')
        val randomCode = (10..99).random().toString()

        // 지점-계좌-랜덤코드
        val accountNumber = "$branchCode-$accountCode-$randomCode"

        if (bankAcoountReadService.isBankAccountNumberExists(accountNumber)) {
            throw ConflictException(message = "계좌 개설중 문제가 발생하였습니다. 다시 시도해주세요")
        }

        return accountNumber
    }

    data class CreatedBankAccount(
        var bankAccountId: Long, // 은행 계좌 id
        var branchId: Long, // 지점 id
        var balance: BigDecimal, // 잔액
        var createdAt: LocalDateTime, // 개설 일자
        val ownerName: String, // 소유주명
        val ownerType: OwnerType, // 소유주 타입
        val bankAccountNumber: String, // 계좌 번호
    )
}
