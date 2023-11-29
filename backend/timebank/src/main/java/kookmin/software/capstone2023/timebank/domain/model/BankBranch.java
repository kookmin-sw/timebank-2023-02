package kookmin.software.capstone2023.timebank.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "bank_branch")
@Getter
@Setter
@AllArgsConstructor
public class BankBranch extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false, updatable = true)
    private String name;

    @OneToMany(mappedBy = "branch")
    private Set<BankAccount> bankAccounts;
}
