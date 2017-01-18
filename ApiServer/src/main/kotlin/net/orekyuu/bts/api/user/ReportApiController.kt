package net.orekyuu.bts.api.user

import net.orekyuu.bts.message.product.SimpleProductInfo
import net.orekyuu.bts.message.report.*
import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.service.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/report")
class ReportApiController {

    @Autowired
    lateinit var reportService: ReportService
    @Autowired
    lateinit var appUserService: AppUserService

    @PostMapping(value = "/create")
    fun createReport(@RequestBody req: CreateReportRequest): ReportInfo {
        val reportInfo = ReportInfo(
                product = SimpleProductInfo(token = req.productToken),
                assign = UserInfo(id = req.assignUserId),
                title = req.title,
                description = req.description,
                version = req.version,
                stacktrace = req.stacktrace,
                log = req.stacktrace,
                runtimeInfo = req.runTimeInfo
        )
        return reportService.createReport(reportInfo)
    }

    @GetMapping(value = "/list")
    fun list(@RequestParam("productId", required = false) productId: Int?, @RequestParam("productToken", required = false) productToken: String?): List<ReportInfo> {
        val user = appUserService.findAppUserFromSecurityContext()!!
        productId?.let {
            return reportService.findFromProductId(user, it)
        }
        productToken?.let {
            return reportService.findFromProductToken(user, it)
        }
        throw BadRequestBodyException("productId and productToken is null")
    }

    @GetMapping(value = "/show")
    fun show(@RequestParam("reportId") reportId: Int): ReportInfo {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return reportService.findById(user, reportId) ?: throw ReportNotFoundException(reportId)
    }

    @PostMapping(value = "/update")
    fun update(@RequestBody req: ReportUpdateRequest): ReportInfo {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return reportService.updateReport(user, req.reportId, req.newDescription, req.newTitle, req.newAssignUserId)
    }

    @PostMapping(value = "/open")
    fun open(@RequestBody req: ReportOpenRequest): SimpleReportInfo {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return reportService.openReport(user, req.reportId)
    }

    @PostMapping(value = "/close")
    fun close(@RequestBody req: ReportCloseRequest): SimpleReportInfo {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return reportService.closeReport(user, req.reportId)
    }

}
