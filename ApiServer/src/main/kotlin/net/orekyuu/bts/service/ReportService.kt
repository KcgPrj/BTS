package net.orekyuu.bts.service

import net.orekyuu.bts.domain.*
import net.orekyuu.bts.message.report.ReportInfo
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

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
        val productId = reportModel.productId
        val product = Product.findById(productId) ?: throw ProductNotFoundException(productId)
        val assignUserId = reportModel.assignUserId
        val assign = AppUser.findById(assignUserId) ?: throw AppUserNotFoundException(assignUserId)
        checkAuthority(product.team, assign)

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
        val product = Product.findById(productId) ?: throw ProductNotFoundException(productId)
        checkAuthority(product.team, requestUser)
        Report.find { ReportTable.product eq product.id }.map { ofReportInfo(it) }
    }

    override fun updateReport(requestUser: AppUser, reportId: Int, newDescription: String, newAssignUserId: Int): ReportInfo = transaction {
        val report = Report.findById(reportId) ?: throw ReportNotFoundException(reportId)
        val team = report.product.team
        checkAuthority(team, requestUser)
        val newAssignUser = AppUser
                .find {
                    TeamUserTable.user
                            .eq(EntityID(newAssignUserId, AppUserTable))
                            .and(TeamUserTable.team.eq(team.id))
                }
                .singleOrNull() ?: throw NotJoinTeamMemberException(newAssignUserId, team)

        report.description = newDescription
        report.assign = newAssignUser
        ofReportInfo(report)
    }
}

class ReportModel(val title: String, val description: String,
                  val assignUserId: Int, val version: String, val stackTrace: String,
                  val log: String, val runtimeInfo: String, val productId: Int)