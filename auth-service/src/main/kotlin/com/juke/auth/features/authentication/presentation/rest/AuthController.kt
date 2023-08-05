package com.juke.auth.features.authentication.presentation.rest

import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.model.value
import com.juke.auth.features.authentication.domain.usecase.AuthUseCase
import com.juke.auth.features.authentication.domain.usecase.RefreshUseCase
import com.juke.auth.features.authentication.presentation.dto.AuthRequest
import com.juke.auth.features.authentication.presentation.dto.RefreshRequest
import com.juke.auth.features.authentication.presentation.mapper.AuthMapper
import com.juke.auth.features.authentication.presentation.mapper.RefreshMapper
import kotlinx.coroutines.reactor.mono
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("api/v1/auth")
class AuthController(
    private val authMapper: AuthMapper,
    private val refreshMapper: RefreshMapper,
    private val authUseCase: AuthUseCase,
    private val refreshUseCase: RefreshUseCase,
) {

    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun auth(
        @Validated @RequestBody request: AuthRequest,
    ): Mono<ResponseEntity<Any>> {
        val params = authMapper.convert(request)
        return mono { authUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok().body(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }

    @PostMapping(
        path = ["/refresh"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun refresh(
        @Validated @RequestBody request: RefreshRequest,
    ): Mono<ResponseEntity<Any>> {
        val params = refreshMapper.convert(request)
        return mono { refreshUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }
}