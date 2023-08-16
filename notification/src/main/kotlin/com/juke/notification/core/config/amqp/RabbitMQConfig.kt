package com.juke.notification.core.config.amqp

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig(
    private val rabbitMQProperties: RabbitMQProperties,
) {

    @Bean
    fun exchange(): DirectExchange {
        return DirectExchange(rabbitMQProperties.exchange)
    }

    @Bean
    fun queue(): Queue {
        return Queue(rabbitMQProperties.queue)
    }

    @Bean
    fun binding(queue: Queue, exchange: DirectExchange): Binding {
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with(rabbitMQProperties.routingKey)
    }
}