package com.juke.notification.feature.mail.presentation.rest

import com.juke.notification.core.domain.model.Data.Error
import com.juke.notification.core.domain.model.Data.Success
import com.juke.notification.core.domain.model.value
import com.juke.notification.feature.mail.domain.usecase.OtpMailUseCase
import com.juke.notification.feature.mail.presentation.dto.OtpMailRequest
import com.juke.notification.feature.mail.presentation.mapper.OtpMailMapper
import kotlinx.coroutines.reactor.mono
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/notification/mail")
class MailController(
    private val otpMailMapper: OtpMailMapper,
    private val otpMailUseCase: OtpMailUseCase,
) {

    @PostMapping("/send")
    fun sendEmail(
        @RequestBody @Validated request: OtpMailRequest
    ): Mono<ResponseEntity<Any>> {
        val params = otpMailMapper.convert(request)
        return mono { otpMailUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }
}