package net.orekyuu.bts

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client

@SpringBootApplication
@EnableOAuth2Client
open class UiServerApplication

fun main(args: Array<String>) {
    SpringApplication.run(UiServerApplication::class.java, *args)
}
