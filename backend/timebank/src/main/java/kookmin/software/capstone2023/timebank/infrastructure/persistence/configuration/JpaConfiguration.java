package kookmin.software.capstone2023.timebank.infrastructure.persistence.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = {"kookmin.software.capstone2023.timebank.domain.model"})
@EnableJpaRepositories(basePackages = {"kookmin.software.capstone2023.timebank.domain.repository"})
public class JpaConfiguration {
    // Any additional Java-specific configurations can be added here
}
