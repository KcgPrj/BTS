package net.orekyuu.bts

import net.orekyuu.bts.config.BtsApplicationConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
open class TestApplicationListener : ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    lateinit var conf: BtsApplicationConfig

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        connectAndCreateTable(conf.jdbcUrl, conf.driver)
    }
}