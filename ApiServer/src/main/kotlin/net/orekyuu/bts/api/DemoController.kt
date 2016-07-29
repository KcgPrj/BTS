package net.orekyuu.bts.api

import net.orekyuu.bts.message.TestData
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class DemoController {

    @GetMapping("/test")
    fun test(): TestData {
        return TestData(name = "hoge")
    }
}
