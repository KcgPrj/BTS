package net.orekyuu.bts

import net.orekyuu.bts.config.BTSResourceServerConfigurer
import net.orekyuu.bts.config.BtsApplicationConfig
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(ApiServerApplication::class, BtsApplicationConfig::class, BTSResourceServerConfigurer::class))
@WebAppConfiguration
class ApiServerApplicationTests {

	@Test
	fun contextLoads() {
	}

}
