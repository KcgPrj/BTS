package net.orekyuu.bts.service

import net.orekyuu.bts.domain.User
import net.orekyuu.bts.domain.UserTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Service
import java.util.*

@Service
interface UserService {

    /**
     * ユーザーを作成
     */
    fun createUser(clientId: String, token: String, userName: String): User

    /**
     * ユーザーを検索
     */
    fun findUser(clientId: String, token: String): User?

    /**
     * IDからユーザーを検索
     */
    fun findById(uuid: UUID): User?
}

class UserServiceImpl : UserService {
    override fun findUser(clientId: String, token: String)
            = transaction {
        User.find { (UserTable.clientId eq clientId) and (UserTable.token eq token) }
                .limit(1)
                .firstOrNull()
    }

    override fun findById(uuid: UUID)
            = transaction { User.findById(uuid) }

    override fun createUser(clientId: String, token: String, userName: String)
            = transaction {
        User.new {
            this.clientId = clientId
            this.token = token
            this.userName = userName
        }
    }

}
