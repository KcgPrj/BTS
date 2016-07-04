package net.orekyuu.bts.api.user

import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.message.user.UserType
import net.orekyuu.bts.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/user")
class UserInfoController {

    @Autowired
    lateinit var userService: UserService

    @RequestMapping(value = "/github", method = arrayOf(RequestMethod.GET))
    fun fromGithub(principal: Principal): UserInfo {
        val user = userService.findAppUserFromGithub(principal.name) ?: throw UserNotFoundException()
        return UserInfo(user.id.value, user.userName, UserType.GITHUB)
    }
}

class UserNotFoundException() : Exception()