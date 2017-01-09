package net.orekyuu.bts.config

import net.orekyuu.bts.domain.*
import net.orekyuu.bts.service.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
open class BtsApplicationConfig {

    @Value("\${spring.datasource.url}")
    lateinit var jdbcUrl: String

    @Value("\${spring.datasource.driverClassName}")
    lateinit var driver: String

    companion object {
        val TABLE_LIST = arrayOf(AppUserTable, GithubUserTable, ProductTable, ReportTable, TeamTable, TeamUserTable)
    }

    @Bean
    // クロスオリジンリクエストを許可する
    open fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurerAdapter() {
            override fun addCorsMappings(registry: CorsRegistry): Unit {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedHeaders("Authorization", "Content-Type")
            }
        }
    }

    @Bean
    open fun userService(): UserService = UserServiceImpl()

    @Bean
    open fun teamService(): TeamService = TeamServiceImpl()

    @Bean
    open fun productService(): ProductService = ProductServiceImpl()

    @Bean
    open fun appUserService(): AppUserService = AppUserServiceImpl()

    @Bean
    open fun reportService(): ReportService = ReportServiceImpl()
}