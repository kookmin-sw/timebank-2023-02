package kookmin.software.capstone2023.timebank.application.service.account;

import kookmin.software.capstone2023.timebank.domain.model.Account;
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
public class AccountFinder {
    private AccountJpaRepository accountJpaRepository;

    @Autowired
    public AccountFinder(AccountJpaRepository accountJpaRepository) {
        this.accountJpaRepository = accountJpaRepository;
    }

    public Account findById(Long accountId) {
        Optional<Account> ret = accountJpaRepository.findById(accountId);
        return ret.orElse(null);
    }
}
