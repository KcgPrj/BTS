package net.orekyuu.bts.message.report

/**
 * @param productToken require
 * @param title optional
 * @param description optional
 * @param version optional
 * @param log optional
 * @param runTimeInfo optional
 * @param stacktrace optional
 */
class CreateReportRequest(
        val productToken: String = "",
        val title: String = "",
        val description: String = "",
        val version: String = "",
        val stacktrace: String = "",
        val log: String = "",
        val runTimeInfo: String = ""
)

class ReportUpdateRequest(
        val reportId: Int = 0,
        val newDescription: String = "",
        val newTitle: String = "",
        val newAssignUserId: Int? = null
)

class ReportOpenRequest(
        val reportId: Int = 0
)

class ReportCloseRequest(
        val reportId: Int = 0
)
