package kookmin.software.capstone2023.timebank.presentation.api.v1.manager;

import kookmin.software.capstone2023.timebank.application.service.auth.AccountLoginService;
import kookmin.software.capstone2023.timebank.application.service.bank.transfer.TransferService;
import kookmin.software.capstone2023.timebank.application.service.bank.transfer.TransferServiceImpl;
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountTransactionJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.spec.BankAccountSpecs;
import kookmin.software.capstone2023.timebank.domain.repository.spec.BankAccountTransactionSpecs;
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext;
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.BankAccountResponseData;
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.BankAccountTransactionResponseData;
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.ManagerLoginRequestData;
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.ManagerLoginResponseData;
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.ManagerPaymentRequestData;
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.ManagerPaymentResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/managers")
public class ManagerController {

    private final AccountLoginService accountLoginService;
    private final BankAccountJpaRepository bankAccountJpaRepository;
    private final BankAccountTransactionJpaRepository bankAccountTransactionJpaRepository;
    private final TransferServiceImpl transferService;

    public ManagerController(AccountLoginService accountLoginService,
                             BankAccountJpaRepository bankAccountJpaRepository,
                             BankAccountTransactionJpaRepository bankAccountTransactionJpaRepository,
                             TransferServiceImpl transferService) {
        this.accountLoginService = accountLoginService;
        this.bankAccountJpaRepository = bankAccountJpaRepository;
        this.bankAccountTransactionJpaRepository = bankAccountTransactionJpaRepository;
        this.transferService = transferService;
    }

    @PostMapping("login")
    public ManagerLoginResponseData loginManager(@Validated @RequestBody ManagerLoginRequestData data) {
        var loginData = accountLoginService.login(data.toAuthenticationRequest());

        return new ManagerLoginResponseData(loginData.getAccessToken());
    }

    @GetMapping("bank-accounts")
    @Transactional(readOnly = true)
    public Page<BankAccountResponseData> listBankAccount(@RequestParam(required = false) String bankAccountNumber,
                                                         @RequestParam(required = false) Long userId,
                                                         @RequestParam(required = false) String userName,
                                                         @RequestParam(required = false) String userPhoneNumber,
                                                         @RequestParam(required = false) LocalDate userBirthday,
                                                         @PageableDefault Pageable pageable) {
        var bankAccounts = bankAccountJpaRepository.findAll(
                Specification.where(BankAccountSpecs.withAccountNumber(bankAccountNumber))
                        .and(BankAccountSpecs.withUser(userId, userName, userPhoneNumber, userBirthday)),
                pageable
        );
        return bankAccounts.map(BankAccountResponseData::fromDomain);
    }

    @GetMapping("{branchId}/transactions")
    @Transactional(readOnly = true)
    public Page<BankAccountTransactionResponseData> listBankAccountTransaction(@PathVariable Long branchId,
                                                                               @RequestParam(required = false) LocalDate startDate,
                                                                               @RequestParam(required = false) LocalDate endDate,
                                                                               @RequestParam(required = false) String name,
                                                                               @PageableDefault Pageable pageable) {
        var transactions = bankAccountTransactionJpaRepository.findAll(
                Specification.where(BankAccountTransactionSpecs.withBranchId(branchId))
                        .and(BankAccountTransactionSpecs.withTransactionAtBetween(startDate, endDate))
                        .and(BankAccountTransactionSpecs.withAccountOwnerName(name)),
                pageable
        );
        return transactions.map(BankAccountTransactionResponseData::fromDomain);
    }

    @UserAuthentication
    @PostMapping("payments")
    @Transactional(readOnly = true)
    public ManagerPaymentResponseData transfer(@RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
                                               @Validated @RequestBody(required = true) ManagerPaymentRequestData paymentRequestData) {
        var response = transferService.transfer(new TransferService.TransferRequest(
                userContext.getAccountId(),
                paymentRequestData.getIsDeposit() ?
                        paymentRequestData.getBranchBankAccountNumber() : paymentRequestData.getUserBankAccountNumber(),
                paymentRequestData.getIsDeposit() ?
                        paymentRequestData.getUserBankAccountNumber() : paymentRequestData.getBranchBankAccountNumber(),
                paymentRequestData.getAmount()));

        if (response.getSenderBankAccount().getAccountNumber().equals(paymentRequestData.getBranchBankAccountNumber())) {
            return new ManagerPaymentResponseData(
                    response.getSenderBankAccount().getAccountNumber(),
                    response.getReceiverBankAccount().getAccountNumber(),
                    response.getCode(),
                    response.getStatus(),
                    response.getAmount(),
                    response.getUpdatedAt());
        } else {
            return new ManagerPaymentResponseData(
                    response.getReceiverBankAccount().getAccountNumber(),
                    response.getSenderBankAccount().getAccountNumber(),
                    response.getCode(),
                    response.getStatus(),
                    response.getAmount(),
                    response.getUpdatedAt());
        }
    }
}
