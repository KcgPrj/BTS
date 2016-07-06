package net.orekyuu.bts.message.report

import net.orekyuu.bts.message.product.SimpleProductInfo
import net.orekyuu.bts.message.user.UserInfo

class ReportInfo(
        var reportId: Int = 0,
        var title: String = "",
        var description: String = "",
        var createdAt: String = "",
        var assign: UserInfo = UserInfo(),
        var version: String = "",
        var stacktrace: String = "",
        var log: String = "",
        var runtimeInfo: String = "",
        var product: SimpleProductInfo
)

class SimpleReportInfo(
        var reportId: Int = 0,
        var title: String = "",
        var createdAt: String = "",
        var assign: UserInfo = UserInfo(),
        var version: String = ""
)