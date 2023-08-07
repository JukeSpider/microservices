package com.juke.auth.features.registration.presentation.rest

import com.juke.auth.core.domain.model.Data.*
import com.juke.auth.core.domain.model.value
import com.juke.auth.features.registration.domain.usecase.CodeValidationUseCase
import com.juke.auth.features.registration.domain.usecase.EmailValidationUseCase
import com.juke.auth.features.registration.presentation.dto.CodeValidationRequest
import com.juke.auth.features.registration.presentation.dto.EmailValidationRequest
import com.juke.auth.features.registration.presentation.mapper.CodeValidationMapper
import com.juke.auth.features.registration.presentation.mapper.EmailValidationMapper
import kotlinx.coroutines.reactor.mono
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/validate")
class RegistrationController (
    private val emailUseCase: EmailValidationUseCase,
    private val emailMapper: EmailValidationMapper,
    private val codeUseCase: CodeValidationUseCase,
    private val codeMapper: CodeValidationMapper,
) {

    @PostMapping("/email")
    fun validateEmail(
        @RequestBody @Validated request: EmailValidationRequest
    ) : Mono<ResponseEntity<Any>> {
        val params = emailMapper.convert(request)
        return mono { emailUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }

    @PostMapping("/code")
    fun validateCode(
        @RequestBody @Validated request: CodeValidationRequest
    ) : Mono<ResponseEntity<Any>> {
        val params = codeMapper.convert(request)
        return mono { codeUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }
}