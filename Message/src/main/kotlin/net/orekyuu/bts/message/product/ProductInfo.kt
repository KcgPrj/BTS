package net.orekyuu.bts.message.product

import net.orekyuu.bts.message.report.SimpleReportInfo

class ProductInfo(
        var productId: Int = 0,
        var productName: String = "",
        var token: String = "",
        var report: List<SimpleReportInfo> = emptyList()
)

class SimpleProductInfo(
        var productId: Int = 0,
        var productName: String = "",
        var token: String = ""
)