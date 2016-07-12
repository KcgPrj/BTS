package net.orekyuu.bts.service

import net.orekyuu.bts.domain.*
import net.orekyuu.bts.message.report.ReportInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 */
interface ReportService {
    /**
     * レポートを生成
     */
    fun createReport(reportModel: ReportModel): ReportInfo

    /**
     * レポートをProductIdから検索
     */
    fun findFromProduct(requestUser: AppUser, productId: Int): List<ReportInfo>

    /**
     * レポートの更新
     */
    fun updateReport(requestUser: AppUser, reportId: Int, newDescription: String, newAssignUserId: Int): ReportInfo
}

class ReportServiceImpl : ReportService {
    override fun createReport(reportModel: ReportModel): ReportInfo = transaction {
        logger.addLogger(StdOutSqlLogger())
        val productToken = reportModel.productToken
        val product = Product
                .find {
                    ProductTable.productToken.eq(UUID.fromString(productToken))
                }
                .singleOrNull() ?: throw ProductNotFoundException(productToken)
        val assignUserId = reportModel.assignUserId
        val assign = getMember(assignUserId, product.team)
        val report = Report.new {
            this.title = reportModel.title
            this.description = reportModel.description
            this.assign = assign
            this.version = reportModel.version
            this.stacktrace = reportModel.stackTrace
            this.log = reportModel.log
            this.runtimeInfo = reportModel.runtimeInfo
            this.product = product
        }
        ofReportInfo(report)
    }

    override fun findFromProduct(requestUser: AppUser, productId: Int): List<ReportInfo> = transaction {
        logger.addLogger(StdOutSqlLogger())
        val product = Product.findById(productId) ?: throw ProductNotFoundException(productId)
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

    private fun getMember(userId: Int, team: Team): AppUser {
        return AppUser.find {
            TeamUserTable.user
                    .eq(EntityID(userId, AppUserTable))
                    .and(TeamUserTable.team.eq(team.id))

        }.singleOrNull() ?: throw NotJoinTeamMemberException(userId, team)
    }
}


class ReportModel(val title: String, val description: String,
                  val assignUserId: Int, val version: String, val stackTrace: String,
                  val log: String, val runtimeInfo: String, val productToken: String)