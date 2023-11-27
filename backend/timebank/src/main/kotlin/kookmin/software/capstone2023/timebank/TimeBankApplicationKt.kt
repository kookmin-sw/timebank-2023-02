package kookmin.software.capstone2023.timebank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@EnableFeignClients
@SpringBootApplication
@ConfigurationPropertiesScan(
    value = ["kookmin.software.capstone2023.timebank.application.configuration"],
)
class TimeBankApplication

fun main() {
    runApplication<TimeBankApplication>()
}
