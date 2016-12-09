package net.orekyuu.bts.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
open class UiServerWebConfig : WebMvcConfigurerAdapter() {

    override fun addViewControllers(registry: ViewControllerRegistry?) {
        super.addViewControllers(registry)
        registry!!.addViewController("/app").setViewName("forward:/app/index.html")
    }
}