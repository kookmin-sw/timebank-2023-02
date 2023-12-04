package kookmin.software.capstone2023.timebank;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
// 외부 API 통신, 외부 API와의 통신을 http 방식으로 진행

@ConfigurationPropertiesScan(
		value = ("kookmin.software.capstone2023.timebank.application.configuration")
)
//@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@SpringBootApplication()
public class TimebankApplication {
	public static void main(String[] args) {
		SpringApplication.run(TimebankApplication.class, args);
	}

}
