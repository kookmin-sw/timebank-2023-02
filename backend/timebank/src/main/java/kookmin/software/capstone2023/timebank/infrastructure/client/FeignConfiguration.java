package kookmin.software.capstone2023.timebank.infrastructure.client;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

public class FeignConfiguration {
    @Bean
    @Profile("dev")
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
