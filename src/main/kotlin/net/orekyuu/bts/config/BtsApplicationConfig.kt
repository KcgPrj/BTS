package net.orekyuu.bts.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
open class BtsApplicationConfig {
    @Value("\${spring.datasource.jdbc-url}")
    lateinit var jdbcUrl: String
    @Value("\${spring.datasource.driverClassName}")
    lateinit var driver: String

}
