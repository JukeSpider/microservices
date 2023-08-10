package com.juke.notification.core.config.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.juke.notification.core.domain.failure.ServiceUnavailableFailure
import kotlinx.coroutines.reactor.mono
import org.slf4j.Logger
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalExceptionHandler(
    private val logger: Logger,
    private val objectMapper: ObjectMapper
) : ErrorWebExceptionHandler {

    override fun handle(exchange: ServerWebExchange, throwable: Throwable): Mono<Void> {

        logger.error("Unhandled global exception", throwable)

        val buffer = mono { objectMapper.writeValueAsBytes(ServiceUnavailableFailure()) }
            .map { exchange.response.bufferFactory().wrap(it) }

        exchange.response.headers.contentType = MediaType.APPLICATION_JSON
        exchange.response.statusCode = HttpStatus.SERVICE_UNAVAILABLE

        return exchange.response.writeWith(buffer)
    }
}