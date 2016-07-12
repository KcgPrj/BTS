package net.orekyuu.bts.service

import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.domain.AppUserTable
import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.message.user.UserType
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.OAuth2Authentication

interface AppUserService {
    /**
     * SecurityContextから現在認証しているユーザーのAppUserを返します
     */
    fun findAppUserFromSecurityContext(): AppUser?

    /**
     * SecurityContextから現在認証しているユーザーのUserInfoを返します
     */
    fun findUserInfoFromSecurityContext(): UserInfo?

    /**
     * idからユーザーを検索
     */
    fun findAppUserById(id: Int): AppUser?
}

class AppUserServiceImpl: AppUserService {
    @Autowired
    private lateinit var userService: UserService

    @Value("\${security.oauth2.client.clientId}")
    private lateinit var githubClientId: String

    override fun findAppUserFromSecurityContext(): AppUser? {
        val auth = SecurityContextHolder.getContext().authentication as? OAuth2Authentication ?: throw RuntimeException("invalid authentication.")
        val type = findUserType(auth.oAuth2Request.clientId)

        return when(type) {
            UserType.GITHUB -> userService.findAppUserFromGithub(auth.name)
        }
    }

    override fun findUserInfoFromSecurityContext(): UserInfo? {
        val auth = SecurityContextHolder.getContext().authentication as? OAuth2Authentication ?: throw RuntimeException("invalid authentication.")
        val type = findUserType(auth.oAuth2Request.clientId)

        when(type) {
            UserType.GITHUB -> {
                val user = userService.findAppUserFromGithub(auth.name)!!
                return UserInfo(user.id.value, user.userName, UserType.GITHUB)
            }
        }
    }

    override fun findAppUserById(id: Int) = transaction { AppUser.findById(id) }

    fun findUserType(clientId: String) = when(clientId) {
        githubClientId -> UserType.GITHUB
        else -> throw IllegalArgumentException(clientId)
    }

}
