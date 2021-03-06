package net.orekyuu.bts.service

import net.orekyuu.bts.domain.*
import net.orekyuu.bts.message.report.ReportInfo
import net.orekyuu.bts.message.report.SimpleReportInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.util.*

/**
 */
interface ReportService {
    /**
     * レポートを生成
     */
    fun createReport(reportInfo: ReportInfo): ReportInfo

    /**
     * レポートをProductIdから検索
     */
    fun findFromProductId(requestUser: AppUser, productId: Int): List<ReportInfo>

    /**
     * レポートをProductTokenから検索
     */
    fun findFromProductToken(requestUser: AppUser, productToken: String): List<ReportInfo>

    /**
     * レポートを見つける
     */
    fun findById(requestUser: AppUser, reportId: Int): ReportInfo?

    /**
     * レポートの更新
     */
    fun updateReport(requestUser: AppUser, reportId: Int, newDescription: String, newTitle: String, newAssignUserId: Int?): ReportInfo

    /**
     * レポートを取得
     */
    fun showReport(requestUser: AppUser, reportId: Int): ReportInfo

    /**
     * レポートをオープン
     */
    fun openReport(requestUser: AppUser, reportId: Int): SimpleReportInfo

    /**
     * レポートをクローズ
     */
    fun closeReport(requestUser: AppUser, reportId: Int): SimpleReportInfo
}

class ReportServiceImpl : ReportService {
    override fun findById(requestUser: AppUser, reportId: Int): ReportInfo? = transaction {
        logger.addLogger(StdOutSqlLogger())
        val report = Report.findById(reportId)
        if (report != null) {
            checkAuthority(report.product.team, requestUser)
            ofReportInfo(report)
        } else {
            null
        }
    }

    override fun createReport(reportInfo: ReportInfo): ReportInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val productToken = reportInfo.product.token
        val product = Product
                .find {
                    ProductTable.productToken eq UUID.fromString(productToken)
                }
                .limit(1)
                .singleOrNull() ?: throw ProductNotFoundException(productToken)
        val assignUserId = reportInfo.assign?.id
        val assign = if (assignUserId == null) null else getMember(assignUserId, product.team)
        val report = Report.new {
            this.title = reportInfo.title
            this.description = reportInfo.description
            this.assign = assign
            this.createdAt = DateTime.now()
            this.version = reportInfo.version
            this.stacktrace = reportInfo.stacktrace
            this.log = reportInfo.log
            this.runtimeInfo = reportInfo.runtimeInfo
            this.product = product
            this.state = ReportState.OPENED
        }
        ofReportInfo(report)
    }

    override fun findFromProductId(requestUser: AppUser, productId: Int): List<ReportInfo> = transaction {
        logger.addLogger(StdOutSqlLogger())
        val product = Product.findById(productId) ?: throw ProductNotFoundException(productId)
        checkAuthority(product.team, requestUser)
        Report.find { ReportTable.product eq product.id }.map(::ofReportInfo)
    }

    override fun findFromProductToken(requestUser: AppUser, productToken: String): List<ReportInfo> = transaction {
        logger.addLogger(StdOutSqlLogger())
        val product = Product
                .find {
                    ProductTable.productToken.eq(UUID.fromString(productToken))
                }
                .limit(1)
                .singleOrNull() ?: throw ProductNotFoundException(productToken)

        checkAuthority(product.team, requestUser)
        Report.find { ReportTable.product eq product.id }.map(::ofReportInfo)
    }

    override fun updateReport(requestUser: AppUser, reportId: Int, newDescription: String, newTitle: String, newAssignUserId: Int?): ReportInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val report = Report.findById(reportId) ?: throw ReportNotFoundException(reportId)
        val team = report.product.team
        checkAuthority(team, requestUser)
        val newAssignUser = if (newAssignUserId == null) null else getMember(newAssignUserId, team)
        report.description = newDescription
        report.assign = newAssignUser
        report.title = newTitle
        ofReportInfo(report)
    }

    override fun showReport(requestUser: AppUser, reportId: Int): ReportInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val report = Report.findById(reportId) ?: throw ReportNotFoundException(reportId)
        checkAuthority(report.product.team, requestUser)
        ofReportInfo(report)
    }


    override fun openReport(requestUser: AppUser, reportId: Int): SimpleReportInfo = transaction {
        val report = Report.findById(reportId) ?: throw ReportNotFoundException(reportId)
        checkAuthority(report.product.team, requestUser)
        val isOpen = report.state == ReportState.OPENED

        if (isOpen)
            throw OpenTriedReportBeenOpenedException(reportId)
        report.state = ReportState.OPENED
        ofSimpleReportInfo(report)
    }

    override fun closeReport(requestUser: AppUser, reportId: Int): SimpleReportInfo = transaction {
        val report = Report.findById(reportId) ?: throw ReportNotFoundException(reportId)
        checkAuthority(report.product.team, requestUser)
        val isClose = report.state == ReportState.CLOSED

        if (isClose)
            throw CloseTriedReportBeenClosedException(reportId)
        report.state = ReportState.CLOSED
        ofSimpleReportInfo(report)
    }

    private fun getMember(userId: Int, team: Team): AppUser {
        val teamUser = TeamUserTable
                .select {
                    TeamUserTable.user
                            .eq(EntityID(userId, AppUserTable))
                            .and(TeamUserTable.team.eq(team.id))

                }
                .limit(1)
                .singleOrNull() ?: throw NotJoinTeamMemberException(userId, team)
        return AppUser[teamUser[TeamUserTable.user]]
    }

}
