package kookmin.software.capstone2023.timebank.application.service.auth.token;

import lombok.Data;

@Data
public class AccessTokenClaims {
    private Long userId;
    private Long accountId;

    public AccessTokenClaims(Long userId, Long accountId) {
        this.userId = userId;
        this.accountId = accountId;
    }
}
