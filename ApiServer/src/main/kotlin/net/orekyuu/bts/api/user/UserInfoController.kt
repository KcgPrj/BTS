package net.orekyuu.bts.api.user

import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.service.AppUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserInfoController {

    @Autowired
    lateinit var userService: AppUserService

    @GetMapping
    fun showUserInfo(): UserInfo {
        return userService.findUserInfoFromSecurityContext() ?: throw UserNotFoundException()
    }
}

class UserNotFoundException() : Exception()