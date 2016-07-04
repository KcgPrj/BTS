package net.orekyuu.bts.domain

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable

object GithubUserTable : IdTable<String>("github_user") {
    override val id = varchar("github_id", 100).primaryKey().entityId()
    val appUser = reference("app_user", AppUserTable)
}

class GithubUser(id: EntityID<String>) : Entity<String>(id) {
    companion object: EntityClass<String, GithubUser>(GithubUserTable)
    var appUser by AppUser.referencedOn(GithubUserTable.appUser)
}
