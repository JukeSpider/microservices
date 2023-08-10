package com.juke.auth.features.registration.domain.feign.client

import com.juke.auth.features.admin.presentation.dto.UserDeletionRequest
import com.juke.auth.features.registration.domain.feign.dto.OtpMailRequest
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import reactivefeign.spring.config.ReactiveFeignClient
import reactor.core.publisher.Mono

@Component
@ReactiveFeignClient(
    name = "notification",
    url = "http://notification:8081/api/v1/mail",
)
interface NotificationClient {

    @RequestMapping(
        path = ["/send"],
        method = [RequestMethod.POST],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun sendEmail(@RequestBody request: OtpMailRequest): Mono<ResponseEntity<Mono<Any>>>
}