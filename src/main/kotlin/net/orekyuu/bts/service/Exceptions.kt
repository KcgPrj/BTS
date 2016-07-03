package net.orekyuu.bts.service

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.client.HttpStatusCodeException

/**
 * 認証が必要なときに投げられる例外
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
class UnauthorizedException(statusText: String = "Authentication is required") : HttpStatusCodeException(HttpStatus.UNAUTHORIZED, statusText)

/**
 * 指定されたユーザーが見つからない時に投げられる例外
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException(statusText: String = "specified user does not exist") : HttpStatusCodeException(HttpStatus.NOT_FOUND)