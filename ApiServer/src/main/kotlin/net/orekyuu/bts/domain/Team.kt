package net.orekyuu.bts.domain

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable

object TeamTable : IdTable<String>("team") {
    override val id = varchar("team_id", 100).primaryKey().entityId()
    val teamName = varchar("teamName", 100)
}

class Team(id: EntityID<String>): Entity<String>(id) {
    companion object: EntityClass<String, Team>(TeamTable)
    var teamName by TeamTable.teamName
    var member by AppUser via TeamUserTable
}