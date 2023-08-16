package com.juke.auth.features.admin.presentation.rest

import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.model.value
import com.juke.auth.features.admin.domain.usecase.UserCreationUseCase
import com.juke.auth.features.admin.domain.usecase.UserDeletionUseCase
import com.juke.auth.features.admin.presentation.dto.UserCreationRequest
import com.juke.auth.features.admin.presentation.dto.UserDeletionRequest
import com.juke.auth.features.admin.presentation.mapper.UserCreationMapper
import com.juke.auth.features.admin.presentation.mapper.UserDeletionMapper
import kotlinx.coroutines.reactor.mono
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/auth/admin")
class AdminController(
    private val userCreationMapper: UserCreationMapper,
    private val userCreationUseCase: UserCreationUseCase,
    private val userDeletionMapper: UserDeletionMapper,
    private val userDeletionUseCase: UserDeletionUseCase,
) {

    @PostMapping("/create-user")
    fun createUser(
        @RequestBody @Validated request: UserCreationRequest,
    ): Mono<ResponseEntity<Any>> {
        val params = userCreationMapper.convert(request)
        return mono { userCreationUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }

    @PostMapping("/delete-user")
    fun deleteUser(
        @RequestBody @Validated request: UserDeletionRequest,
    ): Mono<ResponseEntity<Any>> {
        val params = userDeletionMapper.convert(request)
        return mono { userDeletionUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }
}