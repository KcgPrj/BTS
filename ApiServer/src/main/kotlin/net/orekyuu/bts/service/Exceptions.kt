package net.orekyuu.bts.service

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.client.HttpStatusCodeException

@ResponseStatus(HttpStatus.NOT_FOUND)
open class NotFoundException(statusText: String) : HttpStatusCodeException(HttpStatus.NOT_FOUND, statusText)

@ResponseStatus(HttpStatus.FORBIDDEN)
open class ForbiddenException(statusText: String) : HttpStatusCodeException(HttpStatus.FORBIDDEN, statusText)