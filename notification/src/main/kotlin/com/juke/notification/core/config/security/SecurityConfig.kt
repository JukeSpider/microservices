package com.juke.notification.core.config.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.juke.notification.core.domain.failure.AccessDeniedFailure
import com.juke.notification.core.domain.failure.AuthenticationFailure
import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig(
    private val manager: ReactiveAuthenticationManager,
    private val context: ServerSecurityContextRepository,
) {

    @Bean
    fun encoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityWebFilterChain(
        mapper: ObjectMapper,
        http: ServerHttpSecurity
    ): SecurityWebFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .authenticationManager(manager)
            .securityContextRepository(context)
            .authorizeExchange { ex ->
                ex.pathMatchers("/api/v*/admin", "/api/v*/admin/**").hasRole("ADMIN")
            }
            .authorizeExchange { ex -> ex.anyExchange().permitAll() }
            .exceptionHandling { ex ->
                ex.authenticationEntryPoint { exchanges, _ ->
                    val buffer = mono { mapper.writeValueAsBytes(AuthenticationFailure()) }
                        .map { exchanges.response.bufferFactory().wrap(it) }

                    exchanges.response.headers.contentType = APPLICATION_JSON
                    exchanges.response.statusCode = UNAUTHORIZED
                    exchanges.response.writeWith(buffer)
                }
            }
            .exceptionHandling { ex ->
                ex.accessDeniedHandler { exchanges, _ ->
                    val buffer = mono { mapper.writeValueAsBytes(AccessDeniedFailure()) }
                        .map { exchanges.response.bufferFactory().wrap(it) }

                    exchanges.response.headers.contentType = APPLICATION_JSON
                    exchanges.response.statusCode = FORBIDDEN
                    exchanges.response.writeWith(buffer)
                }
            }

        return http.build()
    }
}