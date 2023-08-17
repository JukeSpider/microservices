package com.juke.notification.feature.mail.domain.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import com.juke.notification.feature.mail.domain.usecase.OtpMailUseCase
import com.juke.notification.feature.mail.presentation.dto.OtpMailRequest
import com.juke.notification.feature.mail.presentation.mapper.OtpMailMapper
import org.slf4j.Logger
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class NotificationConsumer(
    private val otpMailUseCase: OtpMailUseCase,
    private val otpMailMapper: OtpMailMapper,
    private val objectMapper: ObjectMapper,
    private val logger: Logger,
) {

    @RabbitListener(queues = ["notification.queue"])
    suspend fun receiveMessage(message: String) {
        try {
            logger.info("Received message from notification.queue: {}", message)
            val request = objectMapper.readValue(message, OtpMailRequest::class.java)
            val params = otpMailMapper.convert(request)
            otpMailUseCase.invoke(params)
        } catch (t: Throwable) {
            logger.error("Unexpected exception", t)
        }
    }
}