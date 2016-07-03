package net.orekyuu.bts.controller.rest.model

import net.orekyuu.bts.domain.User

data class UserModel(val id: String, val name: String) {
    constructor(user: User) : this(user.id.toString(), user.userName)
}