package net.orekyuu.bts.service

import net.orekyuu.bts.BtsApplication
import net.orekyuu.bts.config.BtsApplicationConfig
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

import org.assertj.core.api.Assertions.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(BtsApplication::class, BtsApplicationConfig::class))
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
    fun findUser() {
        val ret1 = userService.findUser("clientId1", "token1")
        assertThat(ret1).isNull()

        val user1 = userService.createUser("clientId1", "token1", "user1")
        val user2 = userService.createUser("clientId2", "token2", "user2")

        val ret2 = userService.findUser("clientId1", "token1")
        assertThat(ret2?.clientId).isEqualTo("clientId1")
        assertThat(ret2?.token).isEqualTo("token1")
        assertThat(ret2?.userName).isEqualTo("user1")
        assertThat(ret2?.id?.value).isEqualTo(user1.id.value)
    }

    @Test
    fun findById() {
        val user1 = userService.createUser("clientId1", "token1", "user1")
        val user2 = userService.createUser("clientId2", "token2", "user2")

        val result = userService.findById(user1.id.value)
        assertThat(result?.clientId).isEqualTo("clientId1")
        assertThat(result?.token).isEqualTo("token1")
        assertThat(result?.userName).isEqualTo("user1")
        assertThat(result?.id?.value).isEqualTo(user1.id.value)
    }

    @Test
    fun createUser() {
        val user = userService.createUser("clientId", "token", "user1")
        assertThat(user.clientId).isEqualTo("clientId")
        assertThat(user.token).isEqualTo("token")
        assertThat(user.userName).isEqualTo("user1")
        assertThat(user.id.value).isNotNull()
    }

}
