package net.orekyuu.bts.service

import net.orekyuu.bts.ApiServerApplication
import net.orekyuu.bts.config.BTSResourceServerConfigurer
import net.orekyuu.bts.config.BtsApplicationConfig
import org.junit.runner.RunWith
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

import org.assertj.core.api.Assertions.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(ApiServerApplication::class, BtsApplicationConfig::class, BTSResourceServerConfigurer::class))
@WebAppConfiguration
class UserServiceTest {

    @Autowired
    lateinit var userService: UserService

    @Before
    fun setUp() {
        transaction {
            SchemaUtils.create(*BtsApplicationConfig.TABLE_LIST)
        }
    }

    @After
    fun tearDown() {
        transaction {
            SchemaUtils.drop(*BtsApplicationConfig.TABLE_LIST)
        }
    }

    @Test
    fun findAndCreateUserFromGithub() {
        val ret1 = userService.findAppUserFromGithub("github-id")
        assertThat(ret1).isNull()

        val user1 = userService.createAppUserFromGithub("github-id")
        assertThat(user1.userName).isEqualTo("github-id")//デフォルトの表示名はgithubのID

        val ret2 = userService.findAppUserFromGithub("github-id")
        assertThat(ret2?.userName).isEqualTo(user1.userName)
    }

    @Test(expected = DuplicateIDException::class)
    fun duplicateIdUser() {
        userService.createAppUserFromGithub("id")
        userService.createAppUserFromGithub("id")
    }
}