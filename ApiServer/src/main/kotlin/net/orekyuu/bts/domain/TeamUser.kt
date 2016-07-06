package net.orekyuu.bts.domain

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.InsertStatement

object TeamUserTable : Table("team_user") {
    val team = reference("team_id", TeamTable).primaryKey()
    val user = reference("user_id", AppUserTable).primaryKey()

}