package kookmin.software.capstone2023.timebank.presentation.api.v1;

import kookmin.software.capstone2023.timebank.application.service.bank.account.BankAccountReadService;
import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction;
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountTransactionJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.spec.BankAccountTransactionSpecs;
import kookmin.software.capstone2023.timebank.presentation.api.RequestAttributes;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserAuthentication;
import kookmin.software.capstone2023.timebank.presentation.api.auth.model.UserContext;
import kookmin.software.capstone2023.timebank.presentation.api.v1.manager.model.BankAccountTransactionResponseData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@UserAuthentication
@RequestMapping("/api/v1/bank/account/transaction")
@RestController
public class BankAccountTransactionController {

    private final BankAccountReadService bankAccountReadService;
    private final BankAccountTransactionJpaRepository bankAccountTransactionJpaRepository;

    public BankAccountTransactionController(BankAccountReadService bankAccountReadService,
                                            BankAccountTransactionJpaRepository bankAccountTransactionJpaRepository) {
        this.bankAccountReadService = bankAccountReadService;
        this.bankAccountTransactionJpaRepository = bankAccountTransactionJpaRepository;
    }

    @GetMapping("/{bankAccountNumber}")
    public Page<BankAccountTransactionResponseData> getBankAccountTransactionsByBankAccountNumber(
            @RequestAttribute(RequestAttributes.USER_CONTEXT) UserContext userContext,
            @PathVariable String bankAccountNumber,
            @PageableDefault Pageable pageable) {
        var bankAccount = bankAccountReadService.getBankAccountByBankAccountNumber(bankAccountNumber);

        Specification<BankAccountTransaction> spec =
                Specification.where(BankAccountTransactionSpecs.withBankAccountId(bankAccount.getId()));

        return bankAccountTransactionJpaRepository.findAll(spec, pageable)
                .map(BankAccountTransactionResponseData::fromDomain);
    }
}
