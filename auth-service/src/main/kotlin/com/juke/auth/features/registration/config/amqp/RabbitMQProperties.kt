package com.juke.auth.features.registration.config.amqp

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("rabbitmq.notification")
data class RabbitMQProperties(
    val exchange: String,
    val queue: String,
    val routingKey: String,
)