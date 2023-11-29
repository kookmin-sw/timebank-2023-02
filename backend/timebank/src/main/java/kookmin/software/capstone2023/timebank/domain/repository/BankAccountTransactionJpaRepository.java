package kookmin.software.capstone2023.timebank.domain.repository;

import kookmin.software.capstone2023.timebank.domain.model.BankAccountTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountTransactionJpaRepository extends JpaRepository<BankAccountTransaction, Long> {
    Page<BankAccountTransaction> findAll(Specification<BankAccountTransaction> spec, Pageable pageable);
}
