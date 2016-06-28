package net.orekyuu.bts.domain

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import java.util.*

object UserTable : IdTable<UUID>("users") {
    override val id = uuid("user_id").clientDefault { UUID.randomUUID() }.primaryKey().entityId()
    val clientId = varchar("client_id", 128).index()
    val token = varchar("token", 128).index()
    val name = varchar("user_name", 32)
}

class User(id: EntityID<UUID>) : Entity<UUID>(id) {
    companion object: EntityClass<UUID, User>(UserTable)

    var clientId by UserTable.clientId
    var token by UserTable.token
    var userName by UserTable.name
}
