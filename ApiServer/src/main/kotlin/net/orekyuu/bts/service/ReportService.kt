package net.orekyuu.bts.service

import net.orekyuu.bts.domain.*
import net.orekyuu.bts.message.report.ReportInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 */
interface ReportService {
    /**
     * レポートを生成
     */
    fun createReport(reportModel: ReportInfo): ReportInfo

    /**
     * レポートをProductIdから検索
     */
    fun findFromProductId(requestUser: AppUser, productId: Int): List<ReportInfo>

    /**
     * レポートをProductTokenから検索
     */
    fun findFromProductToken(requestUser: AppUser, productToken: String): List<ReportInfo>

    /**
     * レポートの更新
     */
    fun updateReport(requestUser: AppUser, reportId: Int, newDescription: String, newAssignUserId: Int): ReportInfo

    /**
     * レポートを取得
     */
    fun showReport(requestUser: AppUser, reportId: Int): ReportInfo

}

class ReportServiceImpl : ReportService {

    override fun createReport(reportModel: ReportInfo): ReportInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val productToken = reportModel.product.token
        val product = Product
                .find {
                    ProductTable.productToken.eq(UUID.fromString(productToken))
                }
                .limit(1)
                .singleOrNull() ?: throw ProductNotFoundException(productToken)
        val assignUserId = reportModel.assign.id
        val assign = getMember(assignUserId, product.team)
        val report = Report.new {
            this.title = reportModel.title
            this.description = reportModel.description
            this.assign = assign
            this.version = reportModel.version
            this.stacktrace = reportModel.stacktrace
            this.log = reportModel.log
            this.runtimeInfo = reportModel.runtimeInfo
            this.product = product
        }
        ofReportInfo(report)
    }

    override fun findFromProductId(requestUser: AppUser, productId: Int): List<ReportInfo> = transaction {
        logger.addLogger(StdOutSqlLogger())
        val product = Product.findById(productId) ?: throw ProductNotFoundException(productId)
        checkAuthority(product.team, requestUser)
        Report.find { ReportTable.product eq product.id }.map { ofReportInfo(it) }
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
        Report.find { ReportTable.product eq product.id }.map { ofReportInfo(it) }
    }

    override fun updateReport(requestUser: AppUser, reportId: Int, newDescription: String, newAssignUserId: Int): ReportInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val report = Report.findById(reportId) ?: throw ReportNotFoundException(reportId)
        val team = report.product.team
        checkAuthority(team, requestUser)
        val newAssignUser = getMember(newAssignUserId, team)
        report.description = newDescription
        report.assign = newAssignUser
        ofReportInfo(report)
    }

    override fun showReport(requestUser: AppUser, reportId: Int): ReportInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val report = Report.findById(reportId) ?: throw ReportNotFoundException(reportId)
        checkAuthority(report.product.team, requestUser)
        ofReportInfo(report)
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