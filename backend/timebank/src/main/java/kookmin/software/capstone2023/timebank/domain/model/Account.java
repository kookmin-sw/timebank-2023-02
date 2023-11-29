package kookmin.software.capstone2023.timebank.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "account")
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE account SET deleted_at = now() WHERE id = ?")
@Getter
@Setter
public class Account extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(nullable = false, updatable = true, length = 20)
    private String name;

    @Embedded
    private AccountProfile profile;

    @Column(nullable = true, updatable = true)
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Set<User> users;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<BankAccount> bankAccounts;

    public void updateName(String name) {
        this.name = name;
    }

    public Account(AccountType type, AccountProfile profile, String name) {
        this.type = type;
        this.profile = profile;
        this.name = name;
    }
}
