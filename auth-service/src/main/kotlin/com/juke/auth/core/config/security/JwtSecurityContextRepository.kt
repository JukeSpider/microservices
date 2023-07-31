package com.juke.auth.core.config.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtSecurityContextRepository(
    private val jwtManager: JwtAuthenticationManager,
) : ServerSecurityContextRepository {

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        throw UnsupportedOperationException()
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {

        val tokenHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return Mono.empty()
        }

        val token = tokenHeader.substring(7)
        val auth = UsernamePasswordAuthenticationToken(token, token)

        return jwtManager.authenticate(auth).map { s -> SecurityContextImpl(s) }
    }
}