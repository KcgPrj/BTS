package net.orekyuu.bts.service

import net.orekyuu.bts.ApiServerApplication
import net.orekyuu.bts.config.BTSResourceServerConfigurer
import net.orekyuu.bts.config.BtsApplicationConfig
import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.message.team.TeamInfo
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

    lateinit var teamInfo1: TeamInfo
    lateinit var teamInfo2: TeamInfo
    lateinit var teamInfo3: TeamInfo

    @Before
    fun setup() {
        transaction {
            SchemaUtils.create(*BtsApplicationConfig.TABLE_LIST)
        }
        user1 = userService.createAppUserFromGithub("user11")
        user2 = userService.createAppUserFromGithub("user12")

        teamInfo1 = teamService.createTeam(user1, "teamId1", "teamName")
        teamInfo2 = teamService.createTeam(user1, "teamId2", "teamName")
        teamInfo3 = teamService.createTeam(user1, "teamId3", "teamName")

    }

    @After
    fun tearDown() {
        transaction {
            SchemaUtils.drop(*BtsApplicationConfig.TABLE_LIST)
        }
    }

    @Test
    fun registerToTeam() {
        val productName = "registerToTeam"
        val result = productService.registerToTeam(user1, teamInfo1.teamId, productName)
        assert(result.product.size.equals(1))
        assert(result.product[0].productName.equals(productName))

        val productName2 = "registerToTeam2"
        val result2 = productService.registerToTeam(user1, teamInfo1.teamId, productName2)
        assert(result2.product.size.equals(2))
        assert(result2.product[1].productName.equals(productName2))

    }

    @Test(expected = TeamNotFoundException::class)
    fun registerToTeamThrownTeamNotFoundException() {
        productService.registerToTeam(user1, "hoge", "prosuct")
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun registerToTeamThrownTeamAccessAuthorityNotException() {
        productService.registerToTeam(user2, teamInfo1.teamId, "product")
    }

    @Test
    fun deleteFromTeam() {
        val result = productService.registerToTeam(user1, teamInfo2.teamId, "product")
        assert(result.product.size.equals(1))
        val product = result.product[0]
        val result3 = productService.deleteFromTeam(user1, teamInfo2.teamId, product.productId)
        assert(result3.product.isEmpty())
    }

    @Test(expected = TeamNotFoundException::class)
    fun deleteFromTeamThrownTeamNotFoundException() {
        productService.deleteFromTeam(user1, "hoge", 10101010)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun deleteFromTeamThrownTeamAccessAuthorityNotException() {
        productService.deleteFromTeam(user2, teamInfo2.teamId, 2)
    }

    @Test(expected = ProductNotFoundException::class)
    fun deleteFromTeamThrownProductNotFoundException() {
        productService.deleteFromTeam(user1, teamInfo2.teamId, 2)
    }

    @Test
    fun showProductInfo() {
        val result1 = productService.registerToTeam(user1, teamInfo3.teamId, "product")
        val result2 = productService.showProductInfo(user1, teamInfo3.teamId, result1.product[0].productId)
        assert(result2.productName == "product")
    }

    @Test(expected = TeamNotFoundException::class)
    fun showProductInfoThrownTeamNotFoundException() {
        productService.showProductInfo(user1, "hogehoge", 0)
    }

    @Test(expected = ProductNotFoundException::class)
    fun showProductInfoThrownProductNotFoundException() {
        productService.showProductInfo(user1, teamInfo3.teamId, 0)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun showProductInfoThrownTeamAccessAuthorityNotException() {
        productService.showProductInfo(user2, teamInfo3.teamId, 0)
    }
}