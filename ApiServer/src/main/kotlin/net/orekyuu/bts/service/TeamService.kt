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

    override fun joinTeam(introduceUser: AppUser, teamId: String, joinUser: AppUser): TeamInfo {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun defectionTeam(teamId: String, defectionUser: AppUser): TeamInfo {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showTeamInfo(teamId: String, requestUser: AppUser): TeamInfo {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}