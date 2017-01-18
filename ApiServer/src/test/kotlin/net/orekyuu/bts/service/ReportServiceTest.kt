package net.orekyuu.bts.service

import net.orekyuu.bts.config.BtsApplicationConfig
import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.domain.ReportState
import net.orekyuu.bts.message.product.SimpleProductInfo
import net.orekyuu.bts.message.report.ReportInfo
import net.orekyuu.bts.message.team.TeamInfo
import net.orekyuu.bts.message.user.UserInfo
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.util.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
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

    private lateinit var product1: SimpleProductInfo
    private lateinit var product2: SimpleProductInfo
    private lateinit var product3: SimpleProductInfo
    private lateinit var reportModel: ReportInfo

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
        reportModel = ReportInfo(
                title = "title",
                description = "description",
                assign = UserInfo(id = user1.id.value),
                log = "log",
                product = SimpleProductInfo(productId = product1.productId, token = product1.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace",
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
        assertThat(reportInfo.title).isEqualTo(reportModel.title)
        assertThat(reportInfo.assign.id).isEqualTo(reportModel.assign.id)
        assertThat(reportInfo.version).isEqualTo(reportModel.version)
        assertThat(reportInfo.description).isEqualTo(reportModel.description)
        assertThat(reportInfo.log).isEqualTo(reportModel.log)
        assertThat(reportInfo.product.productId).isEqualTo(reportModel.product.productId)
        assertThat(reportInfo.state).isEqualTo(ReportState.OPENED.status)
    }

    @Test(expected = ProductNotFoundException::class)
    fun createReportThrownProductNotFoundException() {
        val reportModel = ReportInfo(
                title = "title",
                description = "description",
                assign = UserInfo(id = user1.id.value),
                log = "log",
                product = SimpleProductInfo(token = UUID.randomUUID().toString()),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace",
                version = "1.0.3")
        reportService.createReport(reportModel)
    }

    @Test(expected = NotJoinTeamMemberException::class)
    fun createReportThrownTeamNotJoinTeamMemberException() {
        val reportModel = ReportInfo(
                title = "title",
                description = "description",
                assign = UserInfo(id = user3.id.value),
                log = "log",
                product = SimpleProductInfo(token = product1.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace",
                version = "1.0.3")
        reportService.createReport(reportModel)
    }

    @Test
    fun findFromProductId() {
        val model1 = ReportInfo(
                title = "title1",
                description = "description1",
                assign = UserInfo(id = user1.id.value),
                log = "log1",
                product = SimpleProductInfo(token = product1.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace1",
                version = "1.0.3")

        val model2 = ReportInfo(
                title = "title2",
                description = "description2",
                assign = UserInfo(id = user2.id.value),
                log = "log2",
                product = SimpleProductInfo(token = product1.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace2",
                version = "1.0.3")

        val model3 = ReportInfo(
                title = "title3",
                description = "description3",
                assign = UserInfo(id = user2.id.value),
                log = "log3",
                product = SimpleProductInfo(token = product2.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace3",
                version = "1.0.3")
        val reportInfo1 = reportService.createReport(model1)
        reportService.createReport(model2)
        val reportInfo2 = reportService.createReport(model3)
        val reportInfoList1 = reportService.findFromProductId(user1, reportInfo1.product.productId)
        assertThat(reportInfoList1.size).isEqualTo(2)
        val reportInfoList2 = reportService.findFromProductId(user1, reportInfo2.product.productId)
        assertThat(reportInfoList2.size).isEqualTo(1)
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
        val model1 = ReportInfo(
                title = "title1",
                description = "description1",
                assign = UserInfo(id = user1.id.value),
                log = "log1",
                product = SimpleProductInfo(token = product1.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace1",
                version = "1.0.3")

        val model2 = ReportInfo(
                title = "title2",
                description = "description2",
                assign = UserInfo(id = user2.id.value),
                log = "log2",
                product = SimpleProductInfo(token = product1.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace2",
                version = "1.0.3")

        val model3 = ReportInfo(
                title = "title3",
                description = "description3",
                assign = UserInfo(id = user2.id.value),
                log = "log3",
                product = SimpleProductInfo(token = product2.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace3",
                version = "1.0.3")
        val reportInfo1 = reportService.createReport(model1)
        reportService.createReport(model2)
        val reportInfo2 = reportService.createReport(model3)
        val reportInfoList1 = reportService.findFromProductToken(user1, reportInfo1.product.token)
        assertThat(reportInfoList1.size).isEqualTo(2)
        val reportInfoList2 = reportService.findFromProductToken(user1, reportInfo2.product.token)
        assertThat(reportInfoList2.size).isEqualTo(1)
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
        val reportInfo2 = reportService.updateReport(user1, reportInfo1.reportId, newDescription, "a", user2.id.value)
        assertThat(reportInfo2.description).isEqualTo(newDescription)
        assertThat(reportInfo2.assign.id).isEqualTo(user2.id.value)
    }

    @Test(expected = ReportNotFoundException::class)
    fun updateReportThrownReportNotFoundException() {
        reportService.updateReport(user1, 101022, "", "", user1.id.value)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun updateReportThrownTeamAccessAuthorityNotException() {
        val reportId = reportService.createReport(reportModel).reportId
        reportService.updateReport(user3, reportId, "", "", user1.id.value)
    }

    @Test(expected = NotJoinTeamMemberException::class)
    fun updateReportThrownNotJoinTeamMemberException() {
        val reportId = reportService.createReport(reportModel).reportId
        reportService.updateReport(user1, reportId, "", "", user3.id.value)
    }

    @Test
    fun showReport() {
        val reportInfo = reportService.createReport(reportModel)
        val reportInfo2 = reportService.showReport(user1, reportInfo.reportId)
        assertThat(reportInfo.reportId).isEqualTo(reportInfo2.reportId)
        assertThat(reportInfo.product.productId).isEqualTo(reportInfo2.product.productId)
        assertThat(reportInfo.description).isEqualTo(reportInfo2.description)
        assertThat(reportInfo.assign.id).isEqualTo(reportInfo2.assign.id)
    }

    @Test(expected = ReportNotFoundException::class)
    fun showReportThrownReportNotFoundException() {
        reportService.showReport(user1, 1010)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun showReportThrown() {
        val reportId = reportService.createReport(reportModel).reportId
        reportService.showReport(user3, reportId)
    }

    @Test
    fun openAndCloseTest() {
        val reportInfo = reportService.createReport(reportModel)
        reportService.closeReport(user1, reportInfo.reportId)
        val stateClose = reportService.showReport(user1, reportInfo.reportId).state
        assertThat(stateClose).isEqualTo(ReportState.CLOSED.status)
        reportService.openReport(user1, reportInfo.reportId)
        val openState = reportService.showReport(user1, reportInfo.reportId).state
        assertThat(openState).isEqualTo(ReportState.OPENED.status)
    }

    @Test(expected = ReportNotFoundException::class)
    fun openReportThrownReportNotFoundException() {
        reportService.openReport(user1, 0)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun openReportThrownTeamAccessAuthorityNotException() {
        val info = reportService.createReport(reportModel)
        reportService.openReport(user3, info.reportId)
    }

    @Test(expected = OpenTriedReportBeenOpenedException::class)
    fun openReportThrownOpenTriedReportBeenOpenedException() {
        val info = reportService.createReport(reportModel)
        reportService.openReport(user1, info.reportId)
    }

    @Test(expected = ReportNotFoundException::class)
    fun closeReportThrownReportNotFoundException() {
        reportService.closeReport(user1, 0)
    }

    @Test(expected = TeamAccessAuthorityNotException::class)
    fun closeReportThrownTeamAccessAuthorityNotException() {
        val info = reportService.createReport(reportModel)
        reportService.closeReport(user3, info.reportId)
    }

    @Test(expected = CloseTriedReportBeenClosedException::class)
    fun closeReportThrownOpenTriedReportBeenOpenedException() {
        val info = reportService.createReport(reportModel)
        reportService.closeReport(user1, info.reportId)
        reportService.closeReport(user1, info.reportId)
    }

}
