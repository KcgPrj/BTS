package net.orekyuu.bts.service

import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.domain.Team
import net.orekyuu.bts.message.team.TeamInfo
import org.jetbrains.exposed.sql.SizedCollection
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.transactions.transaction

interface TeamService {
    /**
     * チームを作成する
     */
    fun createTeam(user: AppUser, teamId: String, teamName: String): TeamInfo

    /**
     * チームにユーザーを追加
     */
    fun joinTeam(introduceUser: AppUser, teamId: String, joinUser: AppUser): TeamInfo

    /**
     * チームから脱退
     */
    fun defectionTeam(teamId: String, defectionUser: AppUser): TeamInfo

    /**
     * チームIDからチームの情報を得る
     */
    fun showTeamInfo(teamId: String, requestUser: AppUser): TeamInfo

    /**
     * チームIDからチームメンバーの一覧を得る
     */
    fun showTeamMember(teamId: String, requestUser: AppUser): List<AppUser>
}

class TeamServiceImpl : TeamService {

    override fun createTeam(user: AppUser, teamId: String, teamName: String): TeamInfo {
        return transaction {
            logger.addLogger(StdOutSqlLogger())

            val team = Team.new(teamId) {
                this.teamName = teamName
            }
            team.member = SizedCollection(listOf(user))
            ofTeamInfo(team)
        }
    }

    override fun joinTeam(introduceUser: AppUser, teamId: String, joinUser: AppUser): TeamInfo = transaction {
        val team: Team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        //introduceUserはチームに含まれているか？
        if (!team.member.any { it.id == introduceUser.id })
            throw TeamAccessAuthorityNotException(introduceUser, team)

        if (!team.member.any { it.id == joinUser.id }) {
            val newMember = team.member.asSequence().plusElement(joinUser).toList()
            team.member = SizedCollection(newMember)
        }
        ofTeamInfo(team)
    }

    override fun defectionTeam(teamId: String, defectionUser: AppUser): TeamInfo = transaction {
        val team: Team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        if (!team.member.any { it.id == defectionUser.id })
            throw NotJoinTeamMemberException(defectionUser, team)

        val newMember = team.member.asSequence().filterNot { it.id == defectionUser.id }.toList()
        team.member = SizedCollection(newMember)
        ofTeamInfo(team)
    }

    override fun showTeamInfo(teamId: String, requestUser: AppUser): TeamInfo = transaction {
        val team: Team = Team.findById(teamId) ?: throw  TeamNotFoundException(teamId)
        if (!team.member.any { it.id == requestUser.id })
            throw TeamAccessAuthorityNotException(requestUser, team)
        ofTeamInfo(team)
    }

    override fun showTeamMember(teamId: String, requestUser: AppUser): List<AppUser> = transaction {
        val team: Team = Team.findById(teamId) ?: throw TeamNotFoundException(teamId)
        if (!team.member.any { it.id == requestUser.id })
            throw TeamAccessAuthorityNotException(requestUser, team)
        team.member.toList()
    }
}