package net.orekyuu.bts.api.user

import net.orekyuu.bts.message.product.*
import net.orekyuu.bts.message.team.TeamInfo
import net.orekyuu.bts.service.AppUserService
import net.orekyuu.bts.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

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
    fun showProduct(@PathVariable("teamId") teamId: String, @PathVariable("id") id: Int): ProductInfo {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return productService.showProduct(user, id)
    }

    @RequestMapping("/create", method = arrayOf(RequestMethod.POST))
    fun createProduct(@PathVariable("teamId") teamId: String, @RequestBody req: CreateProductRequest): TeamInfo {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return productService.registerToTeam(user, teamId, req.productName)
    }

    @RequestMapping("/delete", method = arrayOf(RequestMethod.DELETE))
    fun deleteProduct(@PathVariable("teamId") teamId: String, @RequestBody req: DeleteProductRequest): TeamInfo {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return productService.deleteFromTeam(user, teamId, req.productId)
    }

    @RequestMapping("/update", method = arrayOf(RequestMethod.POST))
    fun update(@PathVariable("teamId") teamId: String, @RequestBody req: ModifyProductRequest): ProductInfo {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return productService.modifyProductName(user, teamId, req.productId, req.newName)
    }

    @RequestMapping("token/regenerate", method = arrayOf(RequestMethod.POST))
    fun tokenRegenerate(@PathVariable("teamId") teamId: String, @RequestBody req: RegenerateTokenRequest): ProductInfo {
        val user = appUserService.findAppUserFromSecurityContext()!!
        return productService.regenerateProductToken(user, teamId, req.productId)
    }

}
