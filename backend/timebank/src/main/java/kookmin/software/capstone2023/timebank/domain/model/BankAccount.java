package kookmin.software.capstone2023.timebank.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bank_account")
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE account SET deleted_at = now() WHERE id = ?")
@Getter
@Setter
@AllArgsConstructor
public class BankAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OwnerType ownerType;

    @Column(nullable = false, updatable = true)
    private String ownerName;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(
            name = "account_id",
            nullable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private Account account;

    @ManyToOne
    @JoinColumn(
            name = "branch_id",
            nullable = false,
            updatable = true,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private BankBranch branch;

    public BankAccount(Account account, BankBranch branch, BigDecimal balance, String ownerName,
                       OwnerType ownerType, String accountNumber, String password) {
        this.account = account;
        this.branch = branch;
        this.balance = balance;
        this.ownerName = ownerName;
        this.ownerType = ownerType;
        this.accountNumber = accountNumber;
        this.password = password;
    }
}
