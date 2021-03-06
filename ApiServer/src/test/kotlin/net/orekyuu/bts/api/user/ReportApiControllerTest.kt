package net.orekyuu.bts.api.user

import net.orekyuu.bts.api.user.util.MockOAuth2Util
import net.orekyuu.bts.api.user.util.printInfo
import net.orekyuu.bts.config.BtsApplicationConfig
import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.message.product.SimpleProductInfo
import net.orekyuu.bts.message.report.ReportInfo
import net.orekyuu.bts.message.team.TeamInfo
import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.service.ProductService
import net.orekyuu.bts.service.ReportService
import net.orekyuu.bts.service.TeamService
import net.orekyuu.bts.service.UserService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@RunWith(SpringJUnit4ClassRunner::class)
@SpringBootTest
class ReportApiControllerTest {
    @Autowired
    lateinit var context: WebApplicationContext
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var teamService: TeamService
    @Autowired
    lateinit var productService: ProductService
    @Autowired
    lateinit var reportService: ReportService

    @Autowired
    lateinit var mockSecurity: MockOAuth2Util


    lateinit var user1: AppUser
    lateinit var user2: AppUser
    lateinit var product: SimpleProductInfo
    lateinit var team: TeamInfo

    lateinit var mock: MockMvc

    @Before
    fun setUp() {
        transaction {
            SchemaUtils.create(*BtsApplicationConfig.TABLE_LIST)
        }

        user1 = userService.createAppUserFromGithub("user1")
        user2 = userService.createAppUserFromGithub("user2")
        team = teamService.createTeam(user1, "teamId", "teamName")
        team = productService.registerToTeam(user1, team.teamId, "product")
        product = team.product[0]
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
    fun list() {
        mockSecurity.enableGithubMock(user1.userName)
        val product = productService.registerToTeam(user1, team.teamId, "").product[0]
        mock.perform(
                get("/api/report/list")
                        .param("productId", product.productId.toString()))
                .andDo(::printInfo)
                .andExpect(status().isOk)

        mock.perform(
                get("/api/report/list")
                        .param("productToken", product.token))
                .andDo(::printInfo)
                .andExpect(status().isOk)

        mock.perform(
                get("/api/report/list"))
                .andDo(::printInfo)
                .andExpect(status().isBadRequest)
    }

    @Test
    fun show() {
        mockSecurity.enableGithubMock(user1.userName)
        val product = productService.registerToTeam(user1, team.teamId, "").product[0]
        val report = reportService.createReport(ReportInfo(0, "test", "desc", "2017/1/1 00:00:00",
                UserInfo(user1.id.value, user1.userName), "version", "stacktrace", "log", "runtime",
                SimpleProductInfo(product.productId, product.productName, product.token)))

        mock.perform(get("/api/report/show").param("reportId", report.reportId.toString())).
                andDo(::printInfo)
                .andExpect(status().isOk)

        mockSecurity.enableGithubMock(user2.userName)
        mock.perform(get("/api/report/show").param("reportId", report.reportId.toString())).
                andDo(::printInfo)
                .andExpect(status().isForbidden)
    }

    @Test
    fun update() {
        mockSecurity.enableGithubMock(user1.userName)
        val team = teamService.createTeam(user1, "hoge", "hoge")
        val id = productService.registerToTeam(user1, team.teamId, "name").product[0].productId
        val product = productService.showProduct(user1, id)
        val info = ReportInfo(
                title = "title",
                description = "description",
                assign = UserInfo(id = user1.id.value),
                log = "log",
                product = SimpleProductInfo(productId = product.productId, token = product.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace",
                version = "1.0.3")
        reportService.createReport(info)
        val report = reportService.findFromProductToken(user1, product.token)
        val updateReq = """{"reportId":${report[0].reportId},"newDescription":"","newAssignUserId":${user1.id}}"""
        mock.perform(
                post("/api/report/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateReq))
                .andDo(::printInfo)
                .andExpect(status().isOk)

        val updateReq2 = """{"reportId":100,"newDescription":"","newAssignUserId":${user1.id}}"""
        mock.perform(
                post("/api/report/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateReq2))
                .andDo(::printInfo)
                .andExpect(status().isNotFound)
    }

    @Test
    fun open() {
        mockSecurity.enableGithubMock(user1.userName)
        val team = teamService.createTeam(user1, "open", "hoge")
        val id = productService.registerToTeam(user1, team.teamId, "name").product[0].productId
        val product = productService.showProduct(user1, id)
        val info = ReportInfo(
                title = "title",
                description = "description",
                assign = UserInfo(id = user1.id.value),
                log = "log",
                product = SimpleProductInfo(productId = product.productId, token = product.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace",
                version = "1.0.3")
        reportService.createReport(info)
        val report = reportService.findFromProductToken(user1, product.token)
        val openRequest = """{"reportId" : ${report[0].reportId}}"""
        mock.perform(post("/api/report/open")
                .contentType(MediaType.APPLICATION_JSON)
                .content(openRequest))
                .andDo(::printInfo)
                .andExpect(status().isBadRequest)
        reportService.closeReport(user1, report[0].reportId)
        mock.perform(post("/api/report/open")
                .contentType(MediaType.APPLICATION_JSON)
                .content(openRequest))
                .andDo(::printInfo)
                .andExpect(status().isOk)
    }

    @Test
    fun close() {
        mockSecurity.enableGithubMock(user1.userName)
        val team = teamService.createTeam(user1, "close", "hoge")
        val id = productService.registerToTeam(user1, team.teamId, "name").product[0].productId
        val product = productService.showProduct(user1, id)
        val info = ReportInfo(
                title = "title",
                description = "description",
                assign = UserInfo(id = user1.id.value),
                log = "log",
                product = SimpleProductInfo(productId = product.productId, token = product.token),
                runtimeInfo = "nougat",
                stacktrace = "stackTrace",
                version = "1.0.3")
        reportService.createReport(info)
        val report = reportService.findFromProductToken(user1, product.token)
        val closeRequest = """{"reportId" : ${report[0].reportId}}"""
        mock.perform(post("/api/report/close")
                .contentType(MediaType.APPLICATION_JSON)
                .content(closeRequest))
                .andDo(::printInfo)
                .andExpect(status().isOk)
        mock.perform(post("/api/report/close")
                .contentType(MediaType.APPLICATION_JSON)
                .content(closeRequest))
                .andDo(::printInfo)
                .andExpect(status().isBadRequest)
    }

}
