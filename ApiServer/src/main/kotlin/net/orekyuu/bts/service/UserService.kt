package net.orekyuu.bts.service

import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.domain.GithubUser
import org.jetbrains.exposed.sql.transactions.transaction

interface UserService {

    fun findAppUserFromGithub(id: String): AppUser?

    fun createAppUserFromGithub(id: String): AppUser
}

class UserServiceImpl : UserService {
    override fun createAppUserFromGithub(id: String): AppUser {
        return transaction {
            if (GithubUser.findById(id) != null) throw DuplicateIDException(id)

            val appUser = AppUser.new { userName = id }
            GithubUser.new(id) {
                this.appUser = appUser
            }
            return@transaction appUser
        }
    }

    override fun findAppUserFromGithub(id: String): AppUser? {
        return transaction {
            GithubUser.findById(id)?.appUser
        }
    }

}

class DuplicateIDException(id: String = ""): Exception("id=$id")