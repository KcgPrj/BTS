package net.orekyuu.bts.domain

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import java.util.*

object ProductTable : IntIdTable("product") {
    val productName = varchar("product_name", 100)
    val productToken = uuid("product_token").clientDefault { UUID.randomUUID() }.index()
    val team = reference("team", TeamTable)
}

class Product(id: EntityID<Int>): Entity<Int>(id) {
    companion object: EntityClass<Int, Product>(ProductTable)
    var productName by ProductTable.productName
    var productToken by ProductTable.productToken
    var team by Team.referencedOn(ProductTable.team)
}