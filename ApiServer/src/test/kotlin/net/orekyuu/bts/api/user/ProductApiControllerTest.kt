package net.orekyuu.bts.api.user

import net.orekyuu.bts.api.user.util.MockOAuth2Util
import net.orekyuu.bts.api.user.util.printInfo
import net.orekyuu.bts.config.BtsApplicationConfig
import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.message.team.TeamInfo
import net.orekyuu.bts.service.ProductService
import net.orekyuu.bts.service.TeamService
import net.orekyuu.bts.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
class ProductApiControllerTest {
    @Autowired
    lateinit var context: WebApplicationContext
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var teamService: TeamService
    @Autowired
    lateinit var productService: ProductService
    @Autowired
    lateinit var mockSecurity: MockOAuth2Util


    lateinit var user1: AppUser
    lateinit var user2: AppUser
    lateinit var teamInfo: TeamInfo
    lateinit var productApiUrl: String

    lateinit var mock: MockMvc

    @Before
    fun setUp() {
        transaction {
            SchemaUtils.create(*BtsApplicationConfig.TABLE_LIST)
        }

        user1 = userService.createAppUserFromGithub("user1")
        user2 = userService.createAppUserFromGithub("user2")
        teamInfo = teamService.createTeam(user1, "teamId", "teamName")
        productApiUrl = "/api/{teamId}/products/"
        mock = MockMvcBuilders
                .webAppContextSetup(context)
                .build()
    }

    @After
    fun tearDown() {
        transaction {
            SchemaUtils.drop(*BtsApplicationConfig.TABLE_LIST)
        }
    }

    @Test
    fun showProducts() {
        mockSecurity.enableGithubMock(user1.userName)
        val createReq = """
        {"productName" : "name"}
        """
        mock.perform(post("$productApiUrl/create", teamInfo.teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReq))
                .andExpect(status().isOk)

        mock.perform(get("$productApiUrl/show", teamInfo.teamId))
                .andDo(::printInfo)
                .andExpect(status().isOk)
        mock.perform(get("$productApiUrl/show", "hoge"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun showProduct() {
        mockSecurity.enableGithubMock(user1.userName)
        val createReq = """
        {"productName" : "name"}
        """
        mock.perform(post("$productApiUrl/create", teamInfo.teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReq))
                .andExpect(status().isOk)
        val product = teamService.showTeamInfo(teamInfo.teamId, user1).product[0]

        mock.perform(get("$productApiUrl/show/{id}", teamInfo.teamId, product.productId))
                .andDo(::printInfo)
                .andExpect(status().isOk)
        mock.perform(get("$productApiUrl/show/{id}", teamInfo.teamId, 1010)).andExpect(status().isNotFound)
    }

    @Test
    fun createProduct() {
        mockSecurity.enableGithubMock(user1.userName)
        val createReq = """
        {"productName" : "name"}
        """
        mock.perform(post("$productApiUrl/create", teamInfo.teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReq))
                .andExpect(status().isOk)

        val products = teamService.showTeamInfo(teamInfo.teamId, user1).product

        assertThat(products.size).isEqualTo(1)
        assertThat(products[0].productName).isEqualTo("name")

        mock.perform(post("$productApiUrl/create", "hoge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReq))
                .andDo(::printInfo)
                .andExpect(status().isNotFound)
    }

    @Test
    fun deleteProduct() {
        mockSecurity.enableGithubMock(user1.userName)
        val createReq = """
        {"productName" : "name"}
        """
        mock.perform(post("$productApiUrl/create", teamInfo.teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReq))
                .andExpect(status().isOk)

        val product = teamService.showTeamInfo(teamInfo.teamId, user1).product[0]
        val deleteReq = """
        {"productId" : ${product.productId}}
        """

        mock.perform(delete("$productApiUrl/delete", teamInfo.teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(deleteReq))
                .andDo(::printInfo)
                .andExpect(status().isOk)

        val teamProducts = teamService.showTeamInfo(teamInfo.teamId, user1).product
        assertThat(teamProducts.size).isEqualTo(0)
    }

    @Test
    fun modify() {
        mockSecurity.enableGithubMock(user1.userName)
        val createReq = """
        {"productName" : "name"}
        """
        mock.perform(post("$productApiUrl/create", teamInfo.teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReq))
                .andExpect(status().isOk)

        val product = teamService.showTeamInfo(teamInfo.teamId, user1).product[0]
        val newName = "newName"
        val modifyReq = """
        {
            "productId" : ${product.productId},
            "newName" : "$newName"
        }
        """
        mock.perform(post("$productApiUrl/update", teamInfo.teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(modifyReq))
                .andDo(::printInfo)
                .andExpect(status().isOk)
        val result = productService.showProduct(user1, product.productId)
        assertThat(result.productName).isEqualTo(newName)
    }

    @Test
    fun tokenRegenerate() {
        mockSecurity.enableGithubMock(user1.userName)
        val product = productService.registerToTeam(user1, teamInfo.teamId, "hoge").product[0]

        val regenerateReq = """
        {
            "productId" : ${product.productId}
        }
        """
        mock.perform(post("$productApiUrl/token/regenerate", teamInfo.teamId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(regenerateReq))
                .andDo(::printInfo)
                .andExpect(status().isOk)

        val result = productService.showProduct(user1, product.productId)
        assertThat(result.token).isNotEqualTo(product.token)
    }


}
