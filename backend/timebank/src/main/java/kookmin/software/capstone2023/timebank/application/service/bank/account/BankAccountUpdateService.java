package kookmin.software.capstone2023.timebank.application.service.bank.account;

import kookmin.software.capstone2023.timebank.domain.model.BankAccount;
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountJpaRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankAccountUpdateService {

    private final BankAccountJpaRepository bankAccountRepository;
    private final BankAccountReadService bankAccountReadService;

    @Transactional
    public UpdatedBankAccount updateBankAccountPassword(
            Long accountId,
            String accountNumber,
            String beforePassword,
            String afterPassword
    ) {
        // 계정에 권한이 있는지 검증
        bankAccountReadService.validateAccountIsBankAccountOwner(accountId, accountNumber);

        // 비밀번호 검증
        bankAccountReadService.validateBankAccountPassword(accountNumber, beforePassword);

        BankAccount bankAccount = bankAccountReadService.getBankAccountByBankAccountNumber(accountNumber);

        bankAccount.setPassword(afterPassword);

        bankAccountRepository.save(bankAccount);

        return new UpdatedBankAccount(
                bankAccount.getAccountNumber(),
                bankAccount.getUpdatedAt()
        );
    }

    @Data
    public static class UpdatedBankAccount {
        private final String bankAccountNumber;
        private final LocalDateTime updatedAt;
    }
}
