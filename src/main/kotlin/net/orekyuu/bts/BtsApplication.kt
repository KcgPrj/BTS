package net.orekyuu.bts

import net.orekyuu.bts.config.BtsApplicationConfig
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class BtsApplication

fun main(args: Array<String>) {
    val context = SpringApplication.run(BtsApplication::class.java, *args)
    val conf = context.getBean(BtsApplicationConfig::class.java)
    connectDatabase(conf.jdbcUrl, conf.driver)

}

fun connectDatabase(jdbcUrl: String, driver: String) {
    Database.connect(jdbcUrl, driver)
    transaction {
        logger.addLogger(StdOutSqlLogger())
        SchemaUtils.create(*BtsApplicationConfig.TABLE_LIST)
    }
}
