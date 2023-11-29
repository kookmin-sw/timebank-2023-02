package kookmin.software.capstone2023.timebank.application.service.user;

import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserWithdrawalService {

    private final UserJpaRepository userJpaRepository;
    private final AccountJpaRepository accountJpaRepository;

    public UserWithdrawalService(UserJpaRepository userJpaRepository, AccountJpaRepository accountJpaRepository) {
        this.userJpaRepository = userJpaRepository;
        this.accountJpaRepository = accountJpaRepository;
    }

    @Transactional
    public void withdrawal(Long userId) {
        var user = userJpaRepository.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        userJpaRepository.delete(user);

        // 개인 계정이면 계정도 삭제
        if (user.getAccount().getType() == AccountType.INDIVIDUAL) {
            accountJpaRepository.delete(user.getAccount());
        }
    }
}
