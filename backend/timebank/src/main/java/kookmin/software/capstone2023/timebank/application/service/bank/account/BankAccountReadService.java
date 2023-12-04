package kookmin.software.capstone2023.timebank.application.service.bank.account;

import kookmin.software.capstone2023.timebank.application.exception.NotFoundException;
import kookmin.software.capstone2023.timebank.application.exception.UnauthorizedException;
import kookmin.software.capstone2023.timebank.domain.model.BankAccount;
import kookmin.software.capstone2023.timebank.domain.model.OwnerType;
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.BankAccountJpaRepository;
import kookmin.software.capstone2023.timebank.infrastructure.security.FailedAttemptsCounter;
import kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.account.PasswordVerificationRequestData;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BankAccountReadService {

    private final BankAccountJpaRepository bankAccountRepository;
    private final AccountJpaRepository accountRepository;
    private final FailedAttemptsCounter failedAttemptsCounter;

    // 은행 계좌 조회
    public ReadedBankAccount readBankAccountByAccountNumber(Long accountId, String bankAccountNumber) {
        // 은행 계좌가 존재하는지 확인. 없으면 404 에러
        BankAccount bankAccount = getBankAccountByBankAccountNumber(bankAccountNumber);

        // 소유주 여부 조회
        boolean isOwner = isOwnerAccountOfBankAccount(accountId, bankAccount);

        ReadedBankAccount readedBankAccount = new ReadedBankAccount(
                bankAccount.getOwnerName(),
                bankAccount.getOwnerType(),
                bankAccount.getAccountNumber()
        );

        if (isOwner) { // 소유주라면
            readedBankAccount.setBankAccountId(bankAccount.getId()); // 은행 계좌 id
            readedBankAccount.setBranchId(bankAccount.getBranch().getId()); // 지점 id
            readedBankAccount.setBalance(bankAccount.getBalance()); // 잔액
            readedBankAccount.setCreatedAt(bankAccount.getCreatedAt()); // 생성일자
        }

        return readedBankAccount;
    }

    // 계정이 가지는 모든 은행 계좌 조회
    public List<ReadedBankAccount> readBankAccountsByAccountId(Long accountId) {
        // 계정이 존재하는지 확인
        validateAccountExists(accountId);

        // 계정이 가지는 모든 은행 계좌 조회
        return bankAccountRepository.findAllByAccountId(accountId).stream()
                .map(it -> new ReadedBankAccount(
                        it.getId(),
                        it.getBranch().getId(),
                        it.getBalance(),
                        it.getCreatedAt(),
                        it.getOwnerName(),
                        it.getOwnerType(),
                        it.getAccountNumber()
                ))
                .toList();
    }

    public BankAccount getBankAccountByBankAccountId(Long bankAccountId) {
        // findById 메서드는 Optional<BankAccount>를 반환하므로 orElseThrow를 사용하여 BankAccount를 반환하거나 에러를 던집니다.
        return bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 은행 계좌입니다."));
    }

    public BankAccount getBankAccountByBankAccountNumber(String bankAccountNumber) {
        return bankAccountRepository.findByAccountNumber(bankAccountNumber)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 은행 계좌입니다."));
    }

    // 계정 존재 여부 조회
    public void validateAccountExists(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new NotFoundException("존재하지 않는 계정입니다.");
        }
    }

    // 계좌 유무 조회
    public boolean isBankAccountNumberExists(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber).isPresent();
    }

    public boolean isOwnerAccountOfBankAccount(Long accountId, BankAccount bankAccount) {
        return bankAccount.getAccount().getId().equals(accountId);
    }

    public void validateAccountIsBankAccountOwner(Long accountId, String bankAccountNumber) {
        // 계정이 존재하는지 확인
        BankAccount bankAccount = getBankAccountByBankAccountNumber(bankAccountNumber);

        // 계정이 소유한 은행 계좌인지 확인
        if (!isOwnerAccountOfBankAccount(accountId, bankAccount)) {
            throw new UnauthorizedException("접근 권한이 없는 은행 계좌입니다.");
        }
    }

    public boolean isPasswordMatched(BankAccount bankAccount, String password) {
        return bankAccount.getPassword().equals(password);
    }

    public void validateBankAccountPassword(String bankAccountNumber, String password) {
        BankAccount bankAccount = getBankAccountByBankAccountNumber(bankAccountNumber);

        if (!isPasswordMatched(bankAccount, password)) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }
    }

    // 은행 계좌 비밀번호 일치여부 반환
    public VerificationResult verifyPassword(PasswordVerificationRequestData request, String ipAddress) {
        BankAccount bankAccount = getBankAccountByBankAccountNumber(request.getBankAccountNumber());

        // 계좌 비밀번호 검증 로직
        if (isPasswordMatched(bankAccount, request.getPassword())) {
            failedAttemptsCounter.resetFailedAttempts(ipAddress);
            return new VerificationResult(1, 0);
        } else {
            failedAttemptsCounter.incrementFailedAttempts(ipAddress);
            int failedAttempts = failedAttemptsCounter.getFailedAttempts(ipAddress);
            return new VerificationResult(0, failedAttempts);
        }
    }

    // 은행 계좌 잔액, 생성 일자, ownerType, branchId, 계좌 번호(필수), 계좌 소유주명(필수) 반환
    @Data
    @AllArgsConstructor
    public static class ReadedBankAccount {
        private Long bankAccountId; // 은행 계좌 id
        private Long branchId; // 지점 id
        private BigDecimal balance; // 잔액
        private LocalDateTime createdAt; // 개설 일자
        private String ownerName; // 소유주명
        private OwnerType ownerType; // 소유주 타입
        private String bankAccountNumber; // 계좌 번호

        public ReadedBankAccount(String ownerName, OwnerType ownerType, String bankAccountNumber) {
            this.ownerName = ownerName;
            this.ownerType = ownerType;
            this.bankAccountNumber = bankAccountNumber;
        }
    }

    @Data
    @AllArgsConstructor
    public static class VerificationResult {
        private final int isPasswordCorrect;
        private final int failedAttempts;
    }
}
