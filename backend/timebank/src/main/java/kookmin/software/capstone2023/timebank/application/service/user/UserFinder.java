package kookmin.software.capstone2023.timebank.application.service.user;

import kookmin.software.capstone2023.timebank.domain.model.User;
import kookmin.software.capstone2023.timebank.domain.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserFinder {

    private final UserJpaRepository userJpaRepository;

    public UserFinder(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    public User findById(Long userId) {
        return userJpaRepository.findById(userId).orElse(null);
    }
    public User findByName(String name) {
        return userJpaRepository.findByName(name).orElse(null);
    }
}
