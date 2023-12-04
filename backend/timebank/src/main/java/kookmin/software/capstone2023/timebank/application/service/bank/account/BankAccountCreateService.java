package kookmin.software.capstone2023.timebank.application.service.bank.account;

import kookmin.software.capstone2023.timebank.application.exception.ConflictException;
import kookmin.software.capstone2023.timebank.application.exception.NotFoundException;
import kookmin.software.capstone2023.timebank.domain.model.*;
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.BankBranchJpaRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankAccountCreateService {

    private final BankAccountReadService bankAccountReadService;
    private final BankAccountJpaRepository bankAccountRepository;
    private final BankBranchJpaRepository bankBranchJpaRepository;
    private final AccountJpaRepository accountRepository;

    @Transactional
    public CreatedBankAccount createBankAccount(Long accountId, String password, Long branchId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("계정을 찾을 수 없습니다."));

        BankBranch branch = bankBranchJpaRepository.findById(branchId)
                .orElseThrow(() -> new NotFoundException("지점이 존재하지 않습니다."));

        BankAccount bankAccount = new BankAccount(
                account,
                branch,
                new BigDecimal(300.0),
                account.getName(),
                (account.getType() == AccountType.INDIVIDUAL) ? OwnerType.USER : OwnerType.BRANCH,
                generateAccountNumber(accountId, branchId),
                password
        );

        // 계좌 생성
        BankAccount createdBankAccount = bankAccountRepository.save(bankAccount);

        return new CreatedBankAccount(
                createdBankAccount.getId(),
                createdBankAccount.getBranch().getId(),
                createdBankAccount.getBalance(),
                createdBankAccount.getCreatedAt(),
                createdBankAccount.getOwnerName(),
                createdBankAccount.getOwnerType(),
                createdBankAccount.getAccountNumber()
        );
    }

    private String generateAccountNumber(Long accountId, Long branchId) {
        String accountCode = String.format("%02d", accountId);
        String branchCode = String.format("%02d", branchId);
        String randomCode = String.valueOf((int) (Math.random() * 90) + 10);

        // 지점-계좌-랜덤코드
        String accountNumber = branchCode + "-" + accountCode + "-" + randomCode;

        if (bankAccountReadService.isBankAccountNumberExists(accountNumber)) {
            throw new ConflictException("계좌 개설중 문제가 발생하였습니다. 다시 시도해주세요");
        }

        return accountNumber;
    }

    @Data
    @AllArgsConstructor
    public static class CreatedBankAccount {
        private Long bankAccountId; // 은행 계좌 id
        private Long branchId; // 지점 id
        private BigDecimal balance; // 잔액
        private LocalDateTime createdAt; // 개설 일자
        private String ownerName; // 소유주명
        private OwnerType ownerType; // 소유주 타입
        private String bankAccountNumber; // 계좌 번호
    }
}
