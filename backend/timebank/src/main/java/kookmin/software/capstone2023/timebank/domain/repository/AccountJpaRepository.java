package kookmin.software.capstone2023.timebank.domain.repository;

import kookmin.software.capstone2023.timebank.domain.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountJpaRepository extends JpaRepository<Account, Long> {
    public Optional<Account> findByName(String name);
}
