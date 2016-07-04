package net.orekyuu.bts.api.open.user

import net.orekyuu.bts.message.user.CreateUserMessage
import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.message.user.UserType
import net.orekyuu.bts.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/open/user/create")
class CreateUserController {

    @Autowired
    lateinit var userService: UserService

    @RequestMapping(value = "/github", method = arrayOf(RequestMethod.POST))
    fun createGithubUser(message: CreateUserMessage): UserInfo {
        val user = userService.createAppUserFromGithub(message.userId)
        return UserInfo(user.id.value, user.userName, UserType.GITHUB)
    }
}
