package kookmin.software.capstone2023.timebank.application.service.auth.model;

import kookmin.software.capstone2023.timebank.domain.model.AccountType;
import kookmin.software.capstone2023.timebank.domain.model.auth.AuthenticationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordAuthenticationRequest extends AuthenticationRequest {
    private final String username;
    private final String password;

    public PasswordAuthenticationRequest(AccountType accountType, String username, String password) {
        super(AuthenticationType.PASSWORD, accountType);
        this.username = username;
        this.password = password;
    }
}