package net.orekyuu.bts.service

import net.orekyuu.bts.ApiServerApplication
import net.orekyuu.bts.config.BTSResourceServerConfigurer
import net.orekyuu.bts.config.BtsApplicationConfig
import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.message.product.ProductInfo
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
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(ApiServerApplication::class, BtsApplicationConfig::class, BTSResourceServerConfigurer::class))
@WebAppConfiguration
class ReportServiceTest {

    @Autowired
    private lateinit var teamService: TeamService

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var reportService: ReportService

    private lateinit var user1: AppUser
    private lateinit var user2: AppUser
    private lateinit var user3: AppUser

    private lateinit var team: TeamInfo

    private lateinit var product1: ProductInfo
    private lateinit var product2: ProductInfo
    private lateinit var product3: ProductInfo
    private lateinit var reportModel: ReportModel

    @Before
    fun setup() {
        transaction {
            SchemaUtils.create(*BtsApplicationConfig.TABLE_LIST)
        }
        user1 = userService.createAppUserFromGithub("user1")
        user2 = userService.createAppUserFromGithub("user2")
        user3 = userService.createAppUserFromGithub("user3")
        team = teamService.createTeam(user1, "ReportServiceTest", "TeamName")
        teamService.joinTeam(user1, team.teamId, user2)
        productService.registerToTeam(user1, team.teamId, "product1")
        productService.registerToTeam(user1, team.teamId, "product2")
        productService.registerToTeam(user1, team.teamId, "product3")
        val products = productService.showProductsFromTeam(user1, team.teamId)
        product1 = products[0]
        product2 = products[1]
        product3 = products[2]
        reportModel = ReportModel(
                title = "title",
                description = "description",
                assignUserId = user1.id.value,
                log = "log",
                productToken = product1.token,
                runtimeInfo = "nougat",
                stackTrace = "stackTrace",
                version = "1.0.3")

    }


    @After
    fun tearDown() {
        transaction {
            SchemaUtils.drop(*BtsApplicationConfig.TABLE_LIST)
        }
    }


    @Test
    fun createReport() {
        val reportInfo = reportService.createReport(reportModel)
        assert(reportInfo.title == reportModel.title)
        assert(reportInfo.description == reportModel.description)
        assert(reportInfo.assign.id == reportModel.assignUserId)
        assert(reportInfo.log == reportModel.log)
        assert(reportInfo.product.token == reportModel.productToken)
        assert(reportInfo.runtimeInfo == reportModel.runtimeInfo)
        assert(reportInfo.stacktrace == reportModel.stackTrace)
        assert(reportInfo.version == reportModel.version)
    }

    @Test(expected = ProductNotFoundException::class)
    fun createReportThrownProductNotFoundException() {
        val reportModel = ReportModel(
                title = "title",
                description = "description",
                assignUserId = user1.id.value,
                log = "log",
                productToken = UUID.randomUUID().toString(),
                runtimeInfo = "nougat",
                stackTrace = "stackTrace",
                version = "1.0.3")
        reportService.createReport(reportModel)
    }

    @Test(expected = NotJoinTeamMemberException::class)
    fun createReportThrownTeamNotJoinTeamMemberException() {
        val reportModel = ReportModel(
                title = "title",
                description = "description",
                assignUserId = user3.id.value,
                log = "log",
                productToken = product1.token,
                runtimeInfo = "nougat",
                stackTrace = "stackTrace",
                version = "1.0.3")
        reportService.createReport(reportModel)
    }

    @Test
    fun findFromProductId() {
        val model1 = ReportModel(
                title = "title1",
                description = "description1",
                assignUserId = user1.id.value,
                log = "log1",
                productToken = product1.token,
                runtimeInfo = "nougat",
                stackTrace = "stackTrace1",
                version = "1.0.3")

        val model2 = ReportModel(
                title = "title2",
                description = "description2",
                assignUserId = user2.id.value,
                log = "log2",
                productToken = product1.token,
                runtimeInfo = "nougat",
                stackTrace = "stackTrace2",
                version = "1.0.3")

        val model3 = ReportModel(
                title = "title3",
                description = "description3",
                assignUserId = user2.id.value,
                log = "log3",
                productToken = product2.token,
                runtimeInfo = "nougat",
                stackTrace = "stackTrace3",
                version = "1.0.3")
        val reportInfo1 = reportService.createReport(model1)
        reportService.createReport(model2)
        val reportInfo2 = reportService.createReport(model3)
        val reportInfoList1 = reportService.findFromProductId(user1, reportInfo1.product.productId)
        assert(reportInfoList1.size == 2)
        val reportInfoList2 = reportService.findFromProductId(user1, reportInfo2.product.productId)
        assert(reportInfoList2.size == 1)
    }

    @Test(expected = ProductNotFoundException::class)
    fun findFromProductIdThrownProductNotFoundException() {
        reportService.findFromProductId(user1, 1010103)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun findFromProductIdThrownTeamAccessAuthorityNotException() {
        reportService.findFromProductId(user3, product1.productId)
    }

    @Test
    fun findFromProductToken() {
        val model1 = ReportModel(
                title = "title1",
                description = "description1",
                assignUserId = user1.id.value,
                log = "log1",
                productToken = product1.token,
                runtimeInfo = "nougat",
                stackTrace = "stackTrace1",
                version = "1.0.3")

        val model2 = ReportModel(
                title = "title2",
                description = "description2",
                assignUserId = user2.id.value,
                log = "log2",
                productToken = product1.token,
                runtimeInfo = "nougat",
                stackTrace = "stackTrace2",
                version = "1.0.3")

        val model3 = ReportModel(
                title = "title3",
                description = "description3",
                assignUserId = user2.id.value,
                log = "log3",
                productToken = product2.token,
                runtimeInfo = "nougat",
                stackTrace = "stackTrace3",
                version = "1.0.3")
        val reportInfo1 = reportService.createReport(model1)
        reportService.createReport(model2)
        val reportInfo2 = reportService.createReport(model3)
        val reportInfoList1 = reportService.findFromProductToken(user1, reportInfo1.product.token)
        assert(reportInfoList1.size == 2)
        val reportInfoList2 = reportService.findFromProductToken(user1, reportInfo2.product.token)
        assert(reportInfoList2.size == 1)
    }

    @Test(expected = ProductNotFoundException::class)
    fun findFromProductTokenThrownProductNotFoundException() {
        reportService.findFromProductToken(user1, UUID.randomUUID().toString())
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun findFromProductTokenThrownTeamAccessAuthorityNotException() {
        reportService.findFromProductToken(user3, product1.token)
    }

    @Test
    fun updateReport() {
        val reportInfo1 = reportService.createReport(reportModel)
        val newDescription = "newDescription"
        val reportInfo2 = reportService.updateReport(user1, reportInfo1.reportId, newDescription, user2.id.value)
        assert(reportInfo2.description == newDescription)
        assert(reportInfo2.assign.id == user2.id.value)
    }

    @Test(expected = ReportNotFoundException::class)
    fun updateReportThrownReportNotFoundException() {
        reportService.updateReport(user1, 101022, "", user1.id.value)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun updateReportThrownTeamAccessAuthorityNotException() {
        val reportId = reportService.createReport(reportModel).reportId
        reportService.updateReport(user3, reportId, "", user1.id.value)
    }

    @Test(expected = NotJoinTeamMemberException::class)
    fun updateReportThrownNotJoinTeamMemberException() {
        val reportId = reportService.createReport(reportModel).reportId
        reportService.updateReport(user1, reportId, "", user3.id.value)
    }
}