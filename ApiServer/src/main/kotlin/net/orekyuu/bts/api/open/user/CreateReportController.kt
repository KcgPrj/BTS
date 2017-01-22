package net.orekyuu.bts.api.open.user

import net.orekyuu.bts.message.product.SimpleProductInfo
import net.orekyuu.bts.message.report.CreateReportRequest
import net.orekyuu.bts.message.report.ReportInfo
import net.orekyuu.bts.service.ReportService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/open/api/report")
class CreateReportController {

    @Autowired
    lateinit var reportService: ReportService

    @PostMapping
    fun createReport(@RequestBody req: CreateReportRequest): ReportInfo {
        val reportInfo = ReportInfo(
                product = SimpleProductInfo(token = req.productToken),
                title = req.title,
                description = req.description,
                version = req.version,
                stacktrace = req.stacktrace,
                log = req.log,
                runtimeInfo = req.runTimeInfo
        )
        return reportService.createReport(reportInfo)
    }
}