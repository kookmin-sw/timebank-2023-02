package kookmin.software.capstone2023.timebank.application.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@ConfigurationProperties(prefix = "application.authentication.test")
public class AuthenticationTestProperties {
    private Boolean enabled = false;

    private UserProperties user = new UserProperties();

    public Boolean getEnabled() { return enabled; }
    public UserProperties getUser() { return user; }
}
