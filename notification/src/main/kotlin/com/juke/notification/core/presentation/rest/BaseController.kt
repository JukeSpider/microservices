package com.juke.notification.core.presentation.rest

import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.net.URI

@RestController
class BaseController {

    @GetMapping("/")
    fun indexController(response: ServerHttpResponse): Mono<Void> {
        response.statusCode = HttpStatus.PERMANENT_REDIRECT
        response.headers.location = URI.create("/webjars/swagger-ui/index.html#/")
        return response.setComplete()
    }
}
