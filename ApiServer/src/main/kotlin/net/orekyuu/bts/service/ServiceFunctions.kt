package net.orekyuu.bts.service

import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.domain.Team
import net.orekyuu.bts.domain.TeamUserTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select

/**
 * AppUserがTeamに含まれているか確認
 * transaction内で使用
 * @param team Team
 * @param appUser AppUser
 * @throws TeamAccessAuthorityNotException [appUser]が[team]に含まれていなかった場合
 */
fun checkAuthority(team: Team, appUser: AppUser) {
    println("---start checkAuthority---")
    //if (!team.member.any { appUser.id == it.id })
    if (TeamUserTable.select { TeamUserTable.team.eq(team.id).and(TeamUserTable.user.eq(appUser.id)) }.empty())
        throw TeamAccessAuthorityNotException(appUser, team)
    println("---end checkAuthority---")
}