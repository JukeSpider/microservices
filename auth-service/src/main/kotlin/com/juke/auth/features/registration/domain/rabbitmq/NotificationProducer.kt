package com.juke.auth.features.registration.domain.rabbitmq

import com.fasterxml.jackson.databind.ObjectMapper
import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.features.registration.config.amqp.RabbitMQProperties
import com.juke.auth.features.registration.domain.feign.dto.OtpMailRequest
import org.slf4j.Logger
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.stereotype.Component

@Component
class NotificationProducer(
    private val amqpTemplate: AmqpTemplate,
    private val objectMapper: ObjectMapper,
    private val logger: Logger,
    private val rabbitMQProperties: RabbitMQProperties,
) {

    fun publish(request: OtpMailRequest): Data<Unit> {
        return try {
            val (exchange, queue, routingKey) = rabbitMQProperties

            val message = objectMapper.writeValueAsString(request)

            amqpTemplate.convertAndSend(exchange, routingKey, message)

            logger.info(
                "Published {} to exchange {} with routing key {}",
                request, exchange, routingKey
            )
            Success(Unit)
        } catch (t: Throwable) {
            Error(ServiceUnavailableFailure())
        }
    }
}