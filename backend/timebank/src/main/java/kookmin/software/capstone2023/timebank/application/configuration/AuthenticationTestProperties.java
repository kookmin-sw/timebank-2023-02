package kookmin.software.capstone2023.timebank.application.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@ConfigurationProperties(prefix = "application.authentication.test")
public class AuthenticationTestProperties {
    private Boolean enabled = true;

    private UserProperties user = new UserProperties();

    public Boolean getEnabled() { return enabled; }
    public UserProperties getUser() { return user; }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setUser(UserProperties user) {
        this.user = user;
    }
}
