package net.orekyuu.bts.service

import net.orekyuu.bts.ApiServerApplication
import net.orekyuu.bts.config.BTSResourceServerConfigurer
import net.orekyuu.bts.config.BtsApplicationConfig
import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.message.team.TeamInfo
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(ApiServerApplication::class, BtsApplicationConfig::class, BTSResourceServerConfigurer::class))
@WebAppConfiguration
class ProductServiceTest {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var teamService: TeamService

    @Autowired
    lateinit var productService: ProductService

    lateinit var user1: AppUser
    lateinit var user2: AppUser

    lateinit var exceptionTestTeamInfo: TeamInfo

    @Before
    fun setup() {
        transaction {
            SchemaUtils.create(*BtsApplicationConfig.TABLE_LIST)
        }
        user1 = userService.createAppUserFromGithub("user11")
        user2 = userService.createAppUserFromGithub("user12")

        exceptionTestTeamInfo = teamService.createTeam(user1, "exceptionTest", "teamName")

    }

    @After
    fun tearDown() {
        transaction {
            SchemaUtils.drop(*BtsApplicationConfig.TABLE_LIST)
        }
    }

    @Test
    fun registerToTeam() {
        val teamInfo = teamService.createTeam(user1, "registerToTeam", "name")
        val productName = "registerToTeam"
        val result = productService.registerToTeam(user1, teamInfo.teamId, productName)
        assert(result.product.size == 1)
        assert(result.product[0].productName.equals(productName))

        val productName2 = "registerToTeam2"
        val result2 = productService.registerToTeam(user1, teamInfo.teamId, productName2)
        assert(result2.product.size == 2)
        assert(result2.product[1].productName.equals(productName2))

    }

    @Test(expected = TeamNotFoundException::class)
    fun registerToTeamThrownTeamNotFoundException() {
        productService.registerToTeam(user1, "hoge", "prosuct")
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun registerToTeamThrownTeamAccessAuthorityNotException() {
        productService.registerToTeam(user2, exceptionTestTeamInfo.teamId, "product")
    }

    @Test
    fun deleteFromTeam() {
        val teamInfo = teamService.createTeam(user1, "deleteFromTeam", "name")
        val result = productService.registerToTeam(user1, teamInfo.teamId, "product")
        assert(result.product.size == 1)
        val product = result.product[0]
        val result3 = productService.deleteFromTeam(user1, teamInfo.teamId, product.productId)
        assert(result3.product.isEmpty())
    }

    @Test(expected = TeamNotFoundException::class)
    fun deleteFromTeamThrownTeamNotFoundException() {
        productService.deleteFromTeam(user1, "hoge", 10101010)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun deleteFromTeamThrownTeamAccessAuthorityNotException() {
        productService.deleteFromTeam(user2, exceptionTestTeamInfo.teamId, 2)
    }

    @Test(expected = ProductNotFoundException::class)
    fun deleteFromTeamThrownProductNotFoundException() {
        productService.deleteFromTeam(user1, exceptionTestTeamInfo.teamId, 2)
    }

    @Test
    fun showProductsFromTeam() {
        val teamInfo = teamService.createTeam(user1, "showProductsFromTeam", "name")
        productService.registerToTeam(user1, teamInfo.teamId, "product1")
        productService.registerToTeam(user1, teamInfo.teamId, "product2")
        val result2 = productService.showProductsFromTeam(user1, teamInfo.teamId)
        assert(result2.size == 2)
    }

    @Test
    fun showProduct() {
        val teamInfo = teamService.createTeam(user1, "showProduct", "name")
        val name = "name"
        val result = productService.registerToTeam(user1, teamInfo.teamId, name)
        val result2 = productService.showProduct(user1, result.product[0].productId)
        assertThat(result2.productId).isEqualTo(result.product[0].productId)
    }

    @Test(expected = ProductNotFoundException::class)
    fun showProductThrownProductNotFoundException() {
        productService.showProduct(user1, 0)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun showProductThrownTeamAccessAuthorityNotException() {
        val teamInfo = teamService.createTeam(user1, "showProduct", "name")
        val result = productService.registerToTeam(user1, teamInfo.teamId, "")
        productService.showProduct(user2,result.product[0].productId)
    }

    @Test(expected = TeamNotFoundException::class)
    fun showProductInfoThrownTeamNotFoundException() {
        val teamInfo = teamService.createTeam(user1, "showProductInfoThrownTeamNotFoundException", "name")
        productService.registerToTeam(user1, teamInfo.teamId, "hoge")
        productService.showProductsFromTeam(user2, "hogehoge")
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun showProductInfoThrownTeamAccessAuthorityNotException() {
        productService.showProductsFromTeam(user2, exceptionTestTeamInfo.teamId)
    }

    @Test
    fun modifyProductName() {
        val teamInfo = teamService.createTeam(user1, "modifyProductName", "name")
        val name1 = "modifyProductName"
        val result1 = productService.registerToTeam(user1, teamInfo.teamId, name1)
        assert(result1.product[0].productName == name1)
        val name2 = "changed"
        val result2 = productService.modifyProductName(user1, teamInfo.teamId, result1.product[0].productId, name2)
        assert(result2.productName == name2)
    }

    @Test(expected = TeamNotFoundException::class)
    fun modifyProductNameThrownTeamNotFoundException() {
        productService.modifyProductName(user1, "hogehoge", 0, "")
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun modifyProductNameThrownTeamAccessAuthorityNotException() {
        productService.modifyProductName(user2, exceptionTestTeamInfo.teamId, 0, "")
    }

    @Test(expected = ProductNotFoundException::class)
    fun modifyProductNameThrownProductNotFoundException() {
        productService.modifyProductName(user1, exceptionTestTeamInfo.teamId, 101010, "")
    }

    @Test
    fun regenerateProductToken() {
        val teamInfo = teamService.createTeam(user1, "regenerateProductToken", "name")
        val name = "regenerateProductToken"
        val result = productService.registerToTeam(user1, teamInfo.teamId, name)
        val product = result.product.find { it.productName == name }!!
        val tokenRegeneratedProduct = productService.regenerateProductToken(user1, teamInfo.teamId, product.productId)
        assert(product.token != tokenRegeneratedProduct.token)
    }

    @Test(expected = TeamNotFoundException::class)
    fun regenerateProductTokenThrownTeamNotFoundException() {
        productService.regenerateProductToken(user1, "hogehoge", 1010)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun regenerateProductTokenThrownTeamAccessAuthorityNotException() {
        productService.regenerateProductToken(user2, exceptionTestTeamInfo.teamId, 1010)
    }

    @Test(expected = ProductNotFoundException::class)
    fun regenerateProductTokenThrownProductNotFoundException() {
        productService.regenerateProductToken(user1, exceptionTestTeamInfo.teamId, 1010)
    }
}