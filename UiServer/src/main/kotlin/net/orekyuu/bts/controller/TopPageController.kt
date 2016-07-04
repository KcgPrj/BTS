package net.orekyuu.bts.controller

import net.orekyuu.bts.message.TestData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
class TopPageController {

    @Autowired
    private lateinit var restTemplate: OAuth2RestTemplate

    @RequestMapping("/")
    fun home(model: Model, resp: HttpServletResponse): String {
        println(restTemplate.accessToken)
        val data = restTemplate.getForEntity("http://localhost:18080/test", TestData::class.java)
        model.addAttribute("data", data.body)
        return "index"
    }
}
