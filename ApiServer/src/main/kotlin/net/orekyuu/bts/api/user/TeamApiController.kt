package net.orekyuu.bts.api.user

import net.orekyuu.bts.message.team.CreateTeamRequest
import net.orekyuu.bts.message.team.DefectionMemberRequest
import net.orekyuu.bts.message.team.JoinMemberRequest
import net.orekyuu.bts.message.team.TeamInfo
import net.orekyuu.bts.message.user.UserInfo
import net.orekyuu.bts.service.AppUserService
import net.orekyuu.bts.service.TeamService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/team")
class TeamApiController {

    @Autowired
    lateinit var teamService: TeamService
    @Autowired
    lateinit var appUserService: AppUserService

    @GetMapping(value = "/show")
    fun showTeam(@RequestParam("teamId") teamId: String): TeamInfo {
        val user = appUserService.findAppUserFromSecurityContext()
        return teamService.showTeamInfo(teamId, user!!)
    }

    @GetMapping(value = "/member/show")
    fun showMember(@RequestParam("teamId") teamId: String): List<UserInfo> {
        val user = appUserService.findAppUserFromSecurityContext()
        return teamService.showTeamMember(teamId, user!!)
    }

    @PostMapping(value = "/create")
    fun createTeam(@RequestBody req: CreateTeamRequest): TeamInfo {
        val user = appUserService.findAppUserFromSecurityContext()
        return teamService.createTeam(
                user!!,
                req.teamId,
                if(req.teamName.isNullOrEmpty()) req.teamId else req.teamName
        )
    }

    @RequestMapping(value = "/member/join", method = arrayOf(RequestMethod.POST), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun join(@RequestBody req: JoinMemberRequest): TeamInfo {
        val user = appUserService.findAppUserFromSecurityContext()
        val joinUser = appUserService.findAppUserById(req.userId) ?: throw UserNotFoundException()
        return teamService.joinTeam(user!!, req.teamId, joinUser)
    }

    @RequestMapping(value = "/member/defection", method = arrayOf(RequestMethod.POST), consumes = arrayOf(MediaType.APPLICATION_JSON_VALUE))
    fun defection(@RequestBody req: DefectionMemberRequest): TeamInfo {
        val user = appUserService.findAppUserFromSecurityContext()
        val defectionUser = appUserService.findAppUserById(req.userId) ?: throw UserNotFoundException()
        return teamService.defectionTeam(user!!, req.teamId, defectionUser)
    }

}
