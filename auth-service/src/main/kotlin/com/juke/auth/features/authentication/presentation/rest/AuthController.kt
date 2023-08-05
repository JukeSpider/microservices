package com.juke.auth.features.authentication.presentation.rest

import com.juke.auth.core.domain.failure.Failure
import com.juke.auth.core.domain.failure.InvalidTokenFailure
import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.model.value
import com.juke.auth.features.authentication.domain.failure.BadCredentialsFailure
import com.juke.auth.features.authentication.domain.failure.RefreshFieldFailure
import com.juke.auth.features.authentication.domain.usecase.AuthUseCase
import com.juke.auth.features.authentication.domain.usecase.RefreshUseCase
import com.juke.auth.features.authentication.presentation.dto.AuthRequest
import com.juke.auth.features.authentication.presentation.dto.AuthResponse
import com.juke.auth.features.authentication.presentation.dto.RefreshRequest
import com.juke.auth.features.authentication.presentation.dto.RefreshResponse
import com.juke.auth.features.authentication.presentation.mapper.AuthMapper
import com.juke.auth.features.authentication.presentation.mapper.RefreshMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Authorization", description = "Авторизация, обновление токенов")
class AuthController(
    private val authMapper: AuthMapper,
    private val refreshMapper: RefreshMapper,
    private val authUseCase: AuthUseCase,
    private val refreshUseCase: RefreshUseCase,
) {

    @Operation(
        summary = "Авторизация пользователя",
        description = """
          Запрос авторизации пользователя
                    
          Требует передачи email и пароля от учётной записи пользователя
          """
    )
    @Parameter(
        `in` = ParameterIn.HEADER,
        name = "Content-Type",
        description = "Стандартный параметр с указанием типа переданного контента",
        content = [
            Content(schema = Schema(type = "string"))
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = """
                    В случае успешной авторизации сервис возвращает связанную пару
                    из access и refresh токенов
                    """,
                content = [
                    Content(schema = Schema(implementation = AuthResponse::class))
                ]
            ),

            ApiResponse(
                responseCode = "400",
                description = """
                    В случае, если формат тела запроса отличается от требуемого, то сервис
                    вернёт следующие ошибки:
                                  
                    1. Отсутствует поле `email`:
                        `"status" = "email_is_missed"`
                        `"message" = "Field <email> is missed!"`
                                  
                    2. Отсутствует поле `password`:
                        `"status" = "password_is_missed"`
                        `"message" = "Field <password> is missed!"`
                    """,
                content = [
                    Content(schema = Schema(implementation = Failure::class))
                ]
            ),

            ApiResponse(
                responseCode = "401",
                description = """
                    В случае, если передана несуществующая связка email-password, то сервис
                    возвращает общую авторизационную ошибку:
                        `"status" = "email_or_password_incorrect"`
                        `"message" = "Email or password incorrect!"`
                    """,
                content = [
                    Content(schema = Schema(implementation = BadCredentialsFailure::class))
                ]
            ),

            ApiResponse(
                responseCode = "500",
                description = """
                    В случае внутренних проблем сервера, сервис вернет общую ошибку
                        `"status" = "service_unavailable"`
                        `"message" = "Service unavailable!"`
                    """,
                content = [
                    Content(schema = Schema(implementation = ServiceUnavailableFailure::class))
                ]
            )]
    )
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

    @Operation(
        summary = "Обновление access-токена",
        description = """
          Запрос на обновление access-токена
                    
          Требует передачи старого refresh-токена
          """
    )
    @Parameter(
        `in` = ParameterIn.HEADER,
        name = "Content-Type",
        description = "Стандартный параметр с указанием типа переданного контента",
        content = [
            Content(schema = Schema(type = "string"))
        ]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = """
                    В случае успешной авторизации сервис возвращает связанную пару
                    из access и refresh токенов
                    """,
                content = [
                    Content(schema = Schema(implementation = RefreshResponse::class))
                ]
            ),

            ApiResponse(
                responseCode = "400",
                description = """
                    В случае, если формат тела запроса отличается от требуемого, то сервис
                    вернёт следующие ошибки:
                                  
                    1. Отсутствует поле `refresh`:
                        `"status" = "refresh_token_is_missed"`
                        `"message" = "Field <refresh> is missed!"`
                    """,
                content = [
                    Content(schema = Schema(implementation = RefreshFieldFailure::class))
                ]
            ),

            ApiResponse(
                responseCode = "401",
                description = """
                    В случаях, если refresh-токен:
                        - не является jwt-токеном;
                        - имеет неверную подпись;
                        - принадлежит удаленному пользователю;
                        - истек,
                    то сервис возвращает общую ошибку:
                        `"status" = "invalid_token"`
                        `"message" = "Token is Invalid!"`
                    """,
                content = [
                    Content(schema = Schema(implementation = InvalidTokenFailure::class))
                ]
            ),

            ApiResponse(
                responseCode = "500",
                description = """
                    В случае внутренних проблем сервера, сервис вернет общую ошибку
                        `"status" = "service_unavailable"`
                        `"message" = "Service unavailable!"`
                    """,
                content = [
                    Content(schema = Schema(implementation = ServiceUnavailableFailure::class))
                ]
            )]
    )
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