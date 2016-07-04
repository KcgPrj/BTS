package net.orekyuu.bts.domain

import org.jetbrains.exposed.dao.*

object AppUserTable : IntIdTable("app_user"){
    val userName = varchar("user_name", 50).index()
}

class AppUser(id: EntityID<Int>) : Entity<Int>(id) {
    companion object: EntityClass<Int, AppUser>(AppUserTable)

    var userName by AppUserTable.userName
}
