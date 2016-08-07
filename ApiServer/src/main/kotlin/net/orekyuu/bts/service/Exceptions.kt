package net.orekyuu.bts.service

import net.orekyuu.bts.domain.AppUser
import net.orekyuu.bts.domain.Team
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.client.HttpStatusCodeException

//HttpStatus系
@ResponseStatus(HttpStatus.NOT_FOUND)
abstract class NotFoundException(statusText: String) : HttpStatusCodeException(HttpStatus.NOT_FOUND, statusText)

@ResponseStatus(HttpStatus.FORBIDDEN)
abstract class ForbiddenException(statusText: String) : HttpStatusCodeException(HttpStatus.FORBIDDEN, statusText)

@ResponseStatus(HttpStatus.BAD_REQUEST)
abstract class BadRequestException(statusText: String) : HttpStatusCodeException(HttpStatus.BAD_REQUEST, statusText)

class BadRequestBodyException(statusText: String) : BadRequestException(statusText)

//Team関係
/**
 * Teamが見つからない時に投げられる
 */
class TeamNotFoundException(id: String) : NotFoundException("specified team[id=$id] does not exist")

/**
 * 指定したuserがTeamに参加していない時に投げられる
 */
class NotJoinTeamMemberException : NotFoundException {
    constructor(appUser: AppUser, team: Team) : super("${appUser.userName} does not join team[id=${team.id},name=${team.teamName}]")
    constructor(userId: Int, team: Team) : super("specified user[$userId] does not join team[id=${team.id},name=${team.teamName}]")
}


class IllegalOperationException(statusText: String) : ForbiddenException(statusText)

/**
 * Teamへのアクセス権が必要なときに投げられる
 */
class TeamAccessAuthorityNotException(appUser: AppUser, team: Team) :
        ForbiddenException("user[id=${appUser.id},name=${appUser.userName}] is no access authority to the specified team[id=${team.id},name=${team.teamName}]")

//Product関係
/**
 * 指定したProductが見つからない時に投げられる
 */
class ProductNotFoundException : NotFoundException {
    constructor(team: Team, productId: Int) : super("is not registered product[id=$productId] in this team[id=${team.id},name=${team.teamName}]")
    constructor(productId: Int) : super("specified product[id=$productId] does not exist")
    constructor(productToken: String) : super("specified product[token=$productToken] does not exist")
}

//Report関係
class ReportNotFoundException(reportId: Int) : NotFoundException("specified report[id=$reportId] does not exist")

class OpenTriedReportBeenOpenedException(reportId: Int) : BadRequestException("tried to open a report[id = $reportId] that has been opened")

class CloseTriedReportBeenClosedException(reportId: Int) : BadRequestException("tried to close a report[id = $reportId] that has been closed")