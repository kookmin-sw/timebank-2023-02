package kookmin.software.capstone2023.timebank.application.service.user;

import kookmin.software.capstone2023.timebank.application.exception.NotFoundException;
import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.domain.model.Gender;
import kookmin.software.capstone2023.timebank.domain.model.User;
import kookmin.software.capstone2023.timebank.domain.model.auth.PasswordAuthentication;
import kookmin.software.capstone2023.timebank.domain.repository.AccountJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.PasswordAuthenticationJpaRepository;
import kookmin.software.capstone2023.timebank.domain.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserUpdateService {

    private final UserJpaRepository userJpaRepository;
    private final AccountJpaRepository accountJpaRepository;
    private final PasswordAuthenticationJpaRepository passwordAuthenticationJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateUserInfo(
            Long userId,
            String name,
            String phoneNumber,
            Gender gender,
            LocalDate birthday
    ) {
        User user = userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        user.updateUserInfo(
                name,
                phoneNumber,
                gender,
                birthday
        );

        // 개인 계정의 경우 계정 이름 변경
        if (user.getAccount().getType() == AccountType.INDIVIDUAL) {
            user.getAccount().updateName(name);
        }
    }

    @Transactional
    public void updatePassword(
            Long userId,
            String password
    ) {
        userJpaRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));

        PasswordAuthentication passwordAuthentication = passwordAuthenticationJpaRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("비밀번호 인증 정보를 찾을 수 없습니다."));

        passwordAuthentication.updatePassword(
                passwordEncoder.encode(password)
        );

        passwordAuthenticationJpaRepository.save(passwordAuthentication);
    }
}
