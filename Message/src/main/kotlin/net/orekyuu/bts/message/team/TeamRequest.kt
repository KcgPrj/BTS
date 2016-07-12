package net.orekyuu.bts.message.team

class CreateTeamRequest(
        val teamId: String = "",
        val teamName: String = ""
)

class JoinMemberRequest(
        val teamId: String = "",
        val userId: Int = 0
)

class DefectionMemberRequest(
        val teamId: String = "",
        val userId: Int = 0
)
