package kookmin.software.capstone2023.timebank.infrastructure.security;

import kookmin.software.capstone2023.timebank.domain.model.security.FailedAttempts;
import kookmin.software.capstone2023.timebank.domain.repository.security.FailedAttemptsRepository;
import org.springframework.stereotype.Component;

@Component
public class FailedAttemptsCounter {
    private final FailedAttemptsRepository failedAttemptsRepository;

    public FailedAttemptsCounter(FailedAttemptsRepository failedAttemptsRepository) {
        this.failedAttemptsRepository = failedAttemptsRepository;
    }

    public void incrementFailedAttempts(String ipAddress) {
        FailedAttempts failedAttempts = failedAttemptsRepository.findByIpAddress(ipAddress);
        if (failedAttempts != null) {
            failedAttempts.setAttempts(failedAttempts.getAttempts() + 1);
            failedAttemptsRepository.save(failedAttempts);
        } else {
            failedAttemptsRepository.save(new FailedAttempts(ipAddress, 1));
        }
    }

    public int getFailedAttempts(String ipAddress) {
        FailedAttempts failedAttempts = failedAttemptsRepository.findByIpAddress(ipAddress);
        return failedAttempts != null ? failedAttempts.getAttempts() : 0;
    }

    public void resetFailedAttempts(String ipAddress) {
        failedAttemptsRepository.deleteById(ipAddress);
    }
}
