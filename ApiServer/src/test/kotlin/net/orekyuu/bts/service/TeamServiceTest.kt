package net.orekyuu.bts.service

import net.orekyuu.bts.ApiServerApplication
import net.orekyuu.bts.config.BTSResourceServerConfigurer
import net.orekyuu.bts.config.BtsApplicationConfig
import net.orekyuu.bts.domain.AppUser
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.web.WebAppConfiguration

import org.assertj.core.api.Assertions.*

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(ApiServerApplication::class, BtsApplicationConfig::class, BTSResourceServerConfigurer::class))
@WebAppConfiguration
class TeamServiceTest {

    @Autowired
    lateinit var teamService: TeamService
    @Autowired
    lateinit var userService: UserService

    lateinit var user1: AppUser
    lateinit var user2: AppUser
    lateinit var user3: AppUser
    @Before
    fun setUp() {
        transaction {
            SchemaUtils.create(*BtsApplicationConfig.TABLE_LIST)
        }

        user1 = userService.createAppUserFromGithub("user1")
        user2 = userService.createAppUserFromGithub("user2")
        user3 = userService.createAppUserFromGithub("user3")
    }

    @After
    fun tearDown() {
        transaction {
            SchemaUtils.drop(*BtsApplicationConfig.TABLE_LIST)
        }
    }

    @Test
    fun createTeam() {
        val result = teamService.createTeam(user1, "team1_id", "team1")
        assertThat(result.teamId).isEqualTo("team1_id")
        assertThat(result.teamName).isEqualTo("team1")
        assertThat(result.member.count()).isEqualTo(1)
        assertThat(result.product.count()).isEqualTo(0)
    }

    @Test
    fun joinTeam() {
        val teamId = "joinTeamTest"
        teamService.createTeam(user1, teamId, "team")
        val result = teamService.joinTeam(user1, teamId, user2)
        assertThat(result.member.count()).isEqualTo(2)
        exceptionTest(TeamNotFoundException::class.java) {
            teamService.joinTeam(user2, "hogehoge", user2)
        }
        exceptionTest(TeamAccessAuthorityNotException::class.java) {
            teamService.joinTeam(user3, teamId, user3)
        }
    }

    @Test
    fun defectionTeam() {
        val teamId = "defectionTeamTest"
        teamService.createTeam(user1, teamId, "team")
        teamService.joinTeam(user1, teamId, user2)
        val result = teamService.joinTeam(user1, teamId, user3)
        assertThat(result.member.count()).isEqualTo(3)
        val result2 = teamService.defectionTeam(teamId, user3)
        assertThat(result2.member.count()).isEqualTo(2)
        exceptionTest(NotJoinTeamMemberException::class.java) {
            teamService.defectionTeam(teamId, user3)
        }
        exceptionTest(TeamNotFoundException::class.java) {
            teamService.defectionTeam("hogehoge", user3)
        }
    }

    @Test
    fun showTeam() {
        val teamId = "showTeamTest"
        val name = "name"
        teamService.createTeam(user1, teamId, name)
        val result = teamService.showTeamInfo(teamId, user1)
        assertThat(result.teamName).isEqualTo(name)
        assertThat(result.member.count()).isEqualTo(1)
        assertThat(result.product.count()).isEqualTo(0)

        exceptionTest(TeamNotFoundException::class.java) {
            teamService.showTeamInfo("hogehoge", user1)
        }

        exceptionTest(TeamAccessAuthorityNotException::class.java) {
            teamService.showTeamInfo(teamId, user2)
        }
    }

    @Test
    fun showTeamMember() {
        val teamId = "showTeamMember"
        teamService.createTeam(user1, teamId, "team")
        val result = teamService.showTeamMember(teamId, user1)
        assertThat(result.count()).isEqualTo(1)
        teamService.joinTeam(user1, teamId, user2)
        val result2 = teamService.showTeamMember(teamId, user1)
        assertThat(result2.count()).isEqualTo(2)
        exceptionTest(TeamNotFoundException::class.java) {
            teamService.showTeamMember("hogehoge", user1)
        }

        exceptionTest(TeamAccessAuthorityNotException::class.java) {
            teamService.showTeamMember(teamId, user3)
        }
    }


    private fun <E : Exception> exceptionTest(exception: Class<E>, func: () -> Unit) {
        try {
            func()
            fail("No exception is thrown")
        } catch (e: E) {
            if (!e.javaClass.equals(exception))
                fail("different from the expected exception")
        }
    }

}
