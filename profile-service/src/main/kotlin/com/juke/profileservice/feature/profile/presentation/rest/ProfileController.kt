package com.juke.profileservice.feature.profile.presentation.rest

import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.feature.profile.domain.usecase.ProfileInfoUseCase
import com.juke.profileservice.feature.profile.domain.usecase.ProfileUpdateUseCase
import com.juke.profileservice.feature.profile.presentation.dto.ProfileUpdateRequest
import com.juke.profileservice.feature.profile.presentation.mapper.ProfileInfoMapper
import com.juke.profileservice.feature.profile.presentation.mapper.ProfileUpdateMapper
import kotlinx.coroutines.reactor.mono
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/profile")
class ProfileController(
    private val infoUseCase: ProfileInfoUseCase,
    private val infoMapper: ProfileInfoMapper,
    private val updateUseCase: ProfileUpdateUseCase,
    private val updateMapper: ProfileUpdateMapper,
) {

    @GetMapping
    fun getProfileInfo(
        @RequestHeader("Authorization") bearerToken: String,
    ): Mono<ResponseEntity<Any>> {
        val params = infoMapper.convert(bearerToken)
        return mono { infoUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.data)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }

    @PutMapping
    fun updateProfile(
        @RequestHeader("Authorization") bearerToken: String,
        @RequestBody @Validated request: ProfileUpdateRequest,
    ): Mono<ResponseEntity<Any>> {
        val params = updateMapper.convert(request.copy(bearerToken = bearerToken))
        return mono { updateUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.data)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }
}