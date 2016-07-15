package net.orekyuu.bts.api.user

import net.orekyuu.bts.message.product.ProductInfo
import net.orekyuu.bts.message.product.SimpleProductInfo
import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.service.AppUserService
import net.orekyuu.bts.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/{teamId}/products")
class ProductApiController {

    @Autowired
    lateinit var appUserService: AppUserService

    @Autowired
    lateinit var productService: ProductService

    @RequestMapping("/show")
    fun showProducts(@PathVariable("teamId") teamId: String): List<SimpleProductInfo> {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return productService.showProductsFromTeam(user, teamId)
    }

    @RequestMapping("/show/{id}")
    fun showProduct(): ProductInfo {
        //TODO
    }
}
