package net.orekyuu.bts

import net.orekyuu.bts.config.BtsApplicationConfig
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

@SpringBootApplication
@EnableResourceServer
open class ApiServerApplication

fun main(args: Array<String>) {
    val context = SpringApplication.run(ApiServerApplication::class.java, *args)
    val conf = context.getBean(BtsApplicationConfig::class.java)
    connectAndCreateTable(conf.jdbcUrl, conf.driver)
}

fun connectAndCreateTable(jdbcUrl: String, driver: String) {
    Database.connect(jdbcUrl, driver)
    transaction {
        logger.addLogger(StdOutSqlLogger())
        SchemaUtils.create(*BtsApplicationConfig.TABLE_LIST)
    }
}
