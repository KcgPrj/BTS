package net.orekyuu.bts.controller.page

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.security.Principal

@Controller
class TopPageController {
    @RequestMapping("/", method = arrayOf(RequestMethod.GET))
    fun top(principal: Principal?, model: Model): String {
        model.addAttribute("principal", principal)
        return "index"
    }
}