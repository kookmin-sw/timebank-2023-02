package kookmin.software.capstone2023.timebank.application.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

//@ConfigurationProperties(prefix = "application.authentication.access-token")
@ConfigurationProperties(prefix = "application.authentication.access-token")
data class AccessTokenProperties(
    val secretKey: String
//    val secretKey: String? = null,
    )
