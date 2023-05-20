package kookmin.software.capstone2023.timebank.presentation.api.v1.manager

import kookmin.software.capstone2023.timebank.application.service.auth.AccountLoginService
import kookmin.software.capstone2023.timebank.application.service.bank.transfer.TransferService
import kookmin.software.capstone2023.timebank.application.service.bank.transfer.TransferServiceImpl
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountJpaRepository
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountTransactionJpaRepository
import kookmin.software.capstone2023.timebank.domain.repository.spec.BankAccountSpecs
import kookmin.software.capstone2023.timebank.domain.repository.spec.BankAccountTransactionSpecs
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.BankAccountResponseData
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.BankAccountTransactionResponseData
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.ManagerLoginRequestData
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.ManagerLoginResponseData
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.ManagerPaymentRequestData
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.ManagerPaymentResponseData
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.web.PageableDefault
import org.springframework.transaction.annotation.Transactional
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("api/v1/managers")
class ManagerController(
    private val accountLoginService: AccountLoginService,
    private val bankAccountJpaRepository: BankAccountJpaRepository,
    private val bankAccountTransactionJpaRepository: BankAccountTransactionJpaRepository,
    private val transferService: TransferServiceImpl,
) {
    @PostMapping("login")
    fun loginManager(
        @Validated @RequestBody
        data: ManagerLoginRequestData,
    ): ManagerLoginResponseData {
        val loginData = accountLoginService.login(data.toAuthenticationRequest())

        return ManagerLoginResponseData(
            accessToken = loginData.accessToken,
        )
    }

    @GetMapping("bank-accounts")
    @Transactional(readOnly = true)
    fun listBankAccount(
        @RequestParam(required = false) bankAccountNumber: String?,
        @RequestParam(required = false) userId: Long?,
        @RequestParam(required = false) userName: String?,
        @RequestParam(required = false) userPhoneNumber: String?,
        @RequestParam(required = false) userBirthday: LocalDate?,
        @PageableDefault pageable: Pageable,
    ): Page<BankAccountResponseData> {
        return bankAccountJpaRepository.findAll(
            Specification.allOf(
                bankAccountNumber?.let { BankAccountSpecs.withAccountNumber(bankAccountNumber) },
                BankAccountSpecs.withUser(
                    id = userId,
                    name = userName,
                    phoneNumber = userPhoneNumber,
                    birthday = userBirthday,
                ),
            ),
            pageable,
        ).map {
            BankAccountResponseData.fromDomain(it)
        }
    }

    /**
     * 은행 계정 거래 내역을 검색하여 페이지네이션을 적용하여 리스트합니다.
     *
     * @param branchId 지점 ID (옵션)
     * @param startDate 검색 시작 날짜 (옵션)
     * @param endDate 검색 종료 날짜 (옵션)
     * @param name 보낸 계정 또는 받은 계정 이름 (옵션)
     * @param pageable 페이지네이션 정보
     * @return 은행 계정 거래 내역 페이지
     */

    @GetMapping("{branchId}/transactions")
    @Transactional(readOnly = true)
    fun listBankAccountTransaction(
        @PathVariable branchId: Long,
        @RequestParam(required = false) startDate: LocalDate?,
        @RequestParam(required = false) endDate: LocalDate?,
        @RequestParam(required = false) name: String?,
        @PageableDefault pageable: Pageable,
    ): Page<BankAccountTransactionResponseData> {
        return bankAccountTransactionJpaRepository.findAll(
            Specification.allOf(
                BankAccountTransactionSpecs.withBranchId(branchId),
                BankAccountTransactionSpecs.withTransactionAtBetween(startDate, endDate),
                BankAccountTransactionSpecs.withAccountOwnerName(name),
            ),
            pageable = pageable,
        ).map {
            BankAccountTransactionResponseData.fromDomain(it)
        }
    }

    /***
     * 은행 계좌 지점 ID/ 지점 보유 은행 계좌/ 지급인지 회수인지/ 거래 금액
     */
    @UserAuthentication
    @PostMapping("payments")
    @Transactional(readOnly = true)
    fun transfer(
        @RequestAttribute(RequestAttributes.USER_CONTEXT) userContext: UserContext,
        @Validated
        @RequestBody(required = true)
        paymentRequestData: ManagerPaymentRequestData,
    ): ManagerPaymentResponseData {
        val response = transferService.transfer(
            TransferService.TransferRequest(
                accountId = userContext.accountId,
                senderAccountNumber = if (paymentRequestData.isDeposit) { // 지급이면
                    paymentRequestData.branchBankAccountNumber
                } else {
                    paymentRequestData.userBankAccountNumber
                },
                receiverAccountNumber = if (paymentRequestData.isDeposit) { // 지급이면
                    paymentRequestData.userBankAccountNumber
                } else {
                    paymentRequestData.branchBankAccountNumber
                },
                amount = paymentRequestData.amount,
            ),
        )

        if (response.senderBankAccount.accountNumber == paymentRequestData.branchBankAccountNumber) { // 지급
            return ManagerPaymentResponseData(
                branchBankAccountNumber = response.senderBankAccount.accountNumber,
                userBankAccountNumber = response.receiverBankAccount.accountNumber,
                amount = response.amount,
                updatedAt = response.updatedAt,
                transactionCode = response.code,
                transactionStatus = response.status,
            )
        } else { // 회수
            return ManagerPaymentResponseData(
                branchBankAccountNumber = response.receiverBankAccount.accountNumber,
                userBankAccountNumber = response.senderBankAccount.accountNumber,
                amount = response.amount,
                updatedAt = response.updatedAt,
                transactionCode = response.code,
                transactionStatus = response.status,
            )
        }
    }
}
