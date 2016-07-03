package net.orekyuu.bts.controller.rest

import net.orekyuu.bts.controller.rest.model.UserModel
import net.orekyuu.bts.service.UnauthorizedException
import net.orekyuu.bts.service.UserNotFoundException
import net.orekyuu.bts.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
class UserRestController {
    @Autowired
    lateinit var userService: UserService

    /**
     * 認証しているユーザー情報を取得する。
     * @param principal Principal
     * @return ユーザーのモデル
     * @throws UnauthorizedException 認証してない状態でリクエストが投げられた時に投げられる。
     * @throws UserNotFoundException ユーザーが見つからなかった時
     */
    @RequestMapping("/api/user/me", method = arrayOf(RequestMethod.GET))
    fun shoeMe(principal: Principal): UserModel {
        val auth = (principal as? OAuth2Authentication) ?: throw UnauthorizedException()
        val user = userService.findUser(auth.oAuth2Request.clientId, "token") ?: throw UserNotFoundException()
        return UserModel(user)
    }
}