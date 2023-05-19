package kookmin.software.capstone2023.timebank.domain.model
import jakarta.persistence.Column
import jakarta.persistence.ConstraintMode
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ForeignKey
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "bank_account_transaction")
data class BankAccountTransaction(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val bankAccountId: Long,

    @Enumerated(EnumType.STRING)
    val code: TransactionCode,

    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    var status: TransactionStatus = TransactionStatus.REQUESTED,

    @ManyToOne
    @JoinColumn(
        name = "receiver_bank_account_id",
        nullable = false,
        updatable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
    )
    val receiverBankAccount: BankAccount,

    @ManyToOne
    @JoinColumn(
        name = "sender_bank_account_id",
        nullable = false,
        updatable = false,
        foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT),
    )
    val senderBankAccount: BankAccount,

    val balanceSnapshot: BigDecimal,

    @Column(nullable = false)
    val transactionAt: LocalDateTime = LocalDateTime.now(),
) : BaseTimeEntity()
