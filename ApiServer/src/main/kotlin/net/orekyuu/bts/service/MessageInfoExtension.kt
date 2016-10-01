//MessageInfoのファクトリ。
//ofXxxを呼び出す場合は必ずtransaction内で使うこと。

package net.orekyuu.bts.service

import net.orekyuu.bts.domain.*
import net.orekyuu.bts.message.product.ProductInfo
import net.orekyuu.bts.message.product.SimpleProductInfo
import net.orekyuu.bts.message.report.ReportInfo
import net.orekyuu.bts.message.report.ReportState
import net.orekyuu.bts.message.report.SimpleReportInfo
import net.orekyuu.bts.message.team.TeamInfo
import net.orekyuu.bts.message.user.UserInfo
import org.jetbrains.exposed.sql.select

fun ofTeamInfo(team: Team, member: Iterable<AppUser> = team.member): TeamInfo {
    val teamProduct = Product.find { ProductTable.team.eq(team.id) }

    return TeamInfo(
            teamId = team.id.value,
            teamName = team.teamName,
            product = teamProduct.map { ofSimpleProductInfo(it) }.toList(),
            member = member.map { ofUserInfo(it) }.toList(),
            owner = ofUserInfo(team.owner)
    )
}

fun ofSimpleTeamInfo(team: Team) = TeamInfo(
        teamId = team.id.value,
        teamName = team.teamName
)

fun ofUserInfo(user: AppUser): UserInfo {
    return UserInfo(user.id.value, user.userName)
}

fun ofProductInfo(product: Product): ProductInfo {
    val report = Report.find { ReportTable.product.eq(product.id) }

    return ProductInfo(
            productId = product.id.value,
            productName = product.productName,
            token = product.productToken.toString(),
            report = report.map { ofSimpleReportInfo(it) }.toList()
    )
}

fun ofSimpleProductInfo(product: Product): SimpleProductInfo {
    return SimpleProductInfo(
            productId = product.id.value,
            productName = product.productName,
            token = product.productToken.toString()
    )
}

fun ofReportInfo(report: Report): ReportInfo {
    val isOpen = !OpenedReport.select { OpenedReport.report eq report.id }.limit(1).empty()
    val state = if (isOpen) ReportState.OPEN else ReportState.CLOSE
    return ReportInfo(
            reportId = report.id.value,
            title = report.title,
            description = report.description,
            createdAt = report.createdAt.toString(),
            assign = ofUserInfo(report.assign),
            version = report.version,
            stacktrace = report.stacktrace,
            log = report.log,
            runtimeInfo = report.runtimeInfo,
            product = ofSimpleProductInfo(report.product),
            state = state
    )
}

fun ofSimpleReportInfo(report: Report): SimpleReportInfo {
    return SimpleReportInfo(
            reportId = report.id.value,
            title = report.title,
            createdAt = report.createdAt.toString(),
            assign = ofUserInfo(report.assign),
            version = report.version
    )
}
