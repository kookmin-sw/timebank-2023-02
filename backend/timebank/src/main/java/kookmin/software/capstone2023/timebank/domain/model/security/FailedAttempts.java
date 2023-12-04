package kookmin.software.capstone2023.timebank.domain.model.security;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "failed_attempts")
@Data
public class FailedAttempts {

    @Id
    private String ipAddress;

    private int attempts;

    public FailedAttempts(String ipAddress, int attempts) {
        this.ipAddress = ipAddress;
        this.attempts = attempts;
    }
}
