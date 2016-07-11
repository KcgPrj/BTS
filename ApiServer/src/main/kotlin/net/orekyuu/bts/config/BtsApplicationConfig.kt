package net.orekyuu.bts.config

import net.orekyuu.bts.domain.*
import net.orekyuu.bts.service.AppUserServiceImpl
import net.orekyuu.bts.service.TeamServiceImpl
import net.orekyuu.bts.service.UserServiceImpl
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
        val TABLE_LIST = arrayOf(AppUserTable, GithubUserTable, ProductTable, ReportTable, TeamTable, TeamUserTable)
    }

    @Bean
    open fun userService() = UserServiceImpl()

    @Bean
    open fun teamService() = TeamServiceImpl()

    @Bean
    open fun appUserService() = AppUserServiceImpl()
}