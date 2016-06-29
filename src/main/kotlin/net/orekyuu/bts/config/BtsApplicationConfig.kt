package net.orekyuu.bts.config

import net.orekyuu.bts.domain.UserTable
import net.orekyuu.bts.service.UserServiceImpl
import org.jetbrains.exposed.sql.Table
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class BtsApplicationConfig {
    @Value("\${spring.datasource.jdbc-url}")
    lateinit var jdbcUrl: String
    @Value("\${spring.datasource.driverClassName}")
    lateinit var driver: String

    companion object {
        val TABLE_LIST: Array<Table> = arrayOf(UserTable)
    }

    @Bean
    open fun userService() = UserServiceImpl()

}
