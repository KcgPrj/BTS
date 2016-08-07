package net.orekyuu.bts.domain

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.joda.time.DateTime

object ReportTable : IntIdTable() {
    /**
     * レポートのタイトル
     */
    val title = varchar("report_title", 100)
    /**
     * 説明文
     */
    val description = text("description")
    /**
     * 作成された日時
     */
    val createdAt = datetime("createdAt").clientDefault { DateTime.now() }
    /**
     * 担当者
     */
    val assign = reference("assign", AppUserTable)
    /**
     * アプリのバージョン
     */
    val version = varchar("version", 100)
    /**
     * スタックトレース
     */
    val stacktrace = text("stacktrace")
    /**
     * ログ
     */
    val log = text("log")
    /**
     * ランタイムの情報(OSなど)
     */
    val runtimeInfo = text("runtime_info")
    /**
     * プロダクト
     */
    val product = reference("product", ProductTable)
}

class Report(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Report>(ReportTable)

    var title by ReportTable.title
    var description by ReportTable.description
    var createdAt by ReportTable.createdAt
    var assign by AppUser.referencedOn(ReportTable.assign)
    var version by ReportTable.version
    var stacktrace by ReportTable.stacktrace
    var log by ReportTable.log
    var runtimeInfo by ReportTable.runtimeInfo
    var product by Product.referencedOn(ReportTable.product)
}

object ClosedReport : Table("closed_table") {
    val report = reference("report_id", ReportTable).primaryKey()
}

object OpenedReport : Table("opened_table") {
    val report = reference("report_id", ReportTable)
}