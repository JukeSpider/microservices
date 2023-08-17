package com.juke.profileservice.core.config.security

import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.core.domain.utils.JwtUtils
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationManager(
    private val jwtUtils: JwtUtils,
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {

        val token = authentication.credentials.toString()

        val credentialsData = jwtUtils.getCredentials(token)
        if (credentialsData is Error) return Mono.empty()

        val credentials = credentialsData.value

        val authResult = UsernamePasswordAuthenticationToken(
            credentials!!.email,
            null,
            mutableListOf(SimpleGrantedAuthority(credentials.role.toString()))
        )

        return mono { authResult }
    }
}