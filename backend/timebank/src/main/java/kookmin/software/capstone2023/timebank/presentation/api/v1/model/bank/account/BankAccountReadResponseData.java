package kookmin.software.capstone2023.timebank.presentation.api.v1.model.bank.account;

import kookmin.software.capstone2023.timebank.domain.model.OwnerType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BankAccountReadResponseData {

    private Long bankAccountId; // 은행 계좌 id
    private Long branchId; // 지점 id
    private BigDecimal balance; // 잔액
    private LocalDateTime createdAt; // 개설 일자
    private final String bankAccountNumber; // 계좌 번호
    private final String ownerName; // 소유주명
    private final OwnerType ownerType; // 소유주 타입

    public BankAccountReadResponseData(String bankAccountNumber, String ownerName, OwnerType ownerType) {
        this.bankAccountNumber = bankAccountNumber;
        this.ownerName = ownerName;
        this.ownerType = ownerType;
    }

    public BankAccountReadResponseData(Long bankAccountId, Long branchId, BigDecimal balance, LocalDateTime createdAt,
                                       String bankAccountNumber, String ownerName, OwnerType ownerType) {
        this.bankAccountId = bankAccountId;
        this.branchId = branchId;
        this.balance = balance;
        this.createdAt = createdAt;
        this.bankAccountNumber = bankAccountNumber;
        this.ownerName = ownerName;
        this.ownerType = ownerType;
    }

    public Long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Long bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public OwnerType getOwnerType() {
        return ownerType;
    }
}
