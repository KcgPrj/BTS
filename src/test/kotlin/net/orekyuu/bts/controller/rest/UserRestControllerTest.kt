package net.orekyuu.bts.controller.rest

import net.orekyuu.bts.BtsApplication
import net.orekyuu.bts.config.BtsApplicationConfig
import net.orekyuu.bts.service.UserService
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.OAuth2Request
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.io.Serializable
import java.security.Principal

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(BtsApplication::class, BtsApplicationConfig::class))
@WebAppConfiguration
class UserRestControllerTest {
    @Autowired
    lateinit var userService: UserService

    private lateinit var mvc: MockMvc
    @Before
    fun setUp() {
        val uc = UserRestController()
        uc.userService = userService
        mvc = MockMvcBuilders.standaloneSetup(uc)
                .build()
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
    fun showMeTest() {
        val user = userService.createUser("clientId", "token", "hoge")
        val principal = Principal { user.userName }
        //認証してない時の
        mvc.perform(get("/api/user/me").principal(principal))
                .andExpect(status().isUnauthorized)


        //モック作りたい...
        val oauthReq = OAuth2Request(
                mapOf<String, String>(),
                "clientId",
                arrayListOf<GrantedAuthority>(),
                false,
                arrayListOf("form").toSet(),
                arrayListOf("ids").toSet(), "redirect",
                arrayListOf("responseTypes").toSet(),
                mapOf<String, Serializable>())

        val authentication = object : Authentication {
            override fun setAuthenticated(isAuthenticated: Boolean) {

            }

            override fun getCredentials(): Any = "hoge"

            override fun isAuthenticated(): Boolean = true

            override fun getDetails(): Any = "hoge"

            override fun getAuthorities(): MutableCollection<out GrantedAuthority> = oauthReq.authorities


            override fun getPrincipal() = principal

            override fun getName() = principal.name
        }

        val oauth = OAuth2Authentication(oauthReq, authentication)

        //認証してる時の
        mvc.perform(get("/api/user/me").principal(oauth))
                .andExpect(status().isOk)

    }
}