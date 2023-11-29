package kookmin.software.capstone2023.timebank.domain.repository;

import kookmin.software.capstone2023.timebank.domain.model.auth.PasswordAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordAuthenticationJpaRepository extends JpaRepository<PasswordAuthentication, Long> {

    Optional<PasswordAuthentication> findByUserId(Long userId);

    PasswordAuthentication findByUsername(String username);

    boolean existsByUsername(String username);
}
