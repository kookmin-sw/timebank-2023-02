package kookmin.software.capstone2023.timebank.domain.repository.security;

import kookmin.software.capstone2023.timebank.domain.model.security.FailedAttempts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedAttemptsRepository extends JpaRepository<FailedAttempts, String> {
    FailedAttempts findByIpAddress(String ipAddress);
}
