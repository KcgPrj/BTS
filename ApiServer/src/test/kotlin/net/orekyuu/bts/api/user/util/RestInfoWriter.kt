package net.orekyuu.bts.api.user.util

import org.springframework.test.web.servlet.MvcResult
import java.util.stream.Collectors

@Throws(Exception::class)
fun printInfo(result: MvcResult) {
    result.request.parameterMap

    println("============= Request =============")
    println("  Path=${result.request.pathInfo}")
    println("  Method=${result.request.method}")
    println()
    println("  ============= Parameter =============")
    result.request.parameterMap.forEach {
        println("    ${it.key}: ${it.value}")
    }
    println("  ============= Parameter =============")
    println()
    println("  ============= Body =============")
    println(result.request.reader?.lines()?.map { "    $it" }?.collect(Collectors.joining("\n")))
    println("  ============= Body =============")
    println("============= Request =============")
    println("============= Response =============")
    println("  " + result.response.contentAsString)
    println("============= Response =============")
}