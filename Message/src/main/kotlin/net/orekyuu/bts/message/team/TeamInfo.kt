package net.orekyuu.bts.message.team

import net.orekyuu.bts.message.product.SimpleProductInfo
import net.orekyuu.bts.message.user.UserInfo

class TeamInfo(
        var teamId: String = "",
        var teamName: String = "",
        var member: List<UserInfo> = emptyList(),
        var product: List<SimpleProductInfo> = emptyList()
)