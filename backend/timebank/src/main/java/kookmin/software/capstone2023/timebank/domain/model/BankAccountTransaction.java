package kookmin.software.capstone2023.timebank.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_account_transaction")
@Getter
@Setter
@AllArgsConstructor
public class BankAccountTransaction extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bankAccountId;

    @Enumerated(EnumType.STRING)
    private TransactionCode code;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status = TransactionStatus.REQUESTED;

    @ManyToOne
    @JoinColumn(
            name = "receiver_bank_account_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private BankAccount receiverBankAccount;

    @ManyToOne
    @JoinColumn(
            name = "sender_bank_account_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private BankAccount senderBankAccount;

    private BigDecimal balanceSnapshot;

    @Column(nullable = false)
    private LocalDateTime transactionAt = LocalDateTime.now();

    public BankAccountTransaction(Long bankAccountId, TransactionCode code, BigDecimal amount, TransactionStatus status,
                                  BankAccount receiverBankAccount, BankAccount senderBankAccount,
                                  BigDecimal balanceSnapshot, LocalDateTime transactionAt) {
        this.bankAccountId = bankAccountId;
        this.code = code;
        this.amount = amount;
        this.status = status;
        this.receiverBankAccount = receiverBankAccount;
        this.senderBankAccount = senderBankAccount;
        this.balanceSnapshot = balanceSnapshot;
        this.transactionAt = transactionAt;
    }
}
