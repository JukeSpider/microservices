package com.juke.auth.features.registration.domain.feign.wrapper

import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.features.registration.domain.feign.client.NotificationClient
import com.juke.auth.features.registration.domain.feign.dto.OtpMailRequest
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.Logger
import org.springframework.stereotype.Component

@Component
class NotificationClientWrapper(
    private val client: NotificationClient,
    private val logger: Logger,
) {

    suspend fun sendEmail(request: OtpMailRequest): Data<Unit> {
        return try {
            var response = client.sendEmail(request).awaitSingle()
            Success(Unit)
        } catch (t: Throwable) {
            logger.error("Unexpected error", t)
            Error(ServiceUnavailableFailure())
        }
    }
}