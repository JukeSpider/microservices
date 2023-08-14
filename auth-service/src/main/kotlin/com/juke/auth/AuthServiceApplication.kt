package com.juke.auth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import reactivefeign.spring.config.EnableReactiveFeignClients

@ConfigurationPropertiesScan
@EnableReactiveFeignClients
@EnableR2dbcRepositories
@EnableDiscoveryClient
@SpringBootApplication
class AuthServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthServiceApplication>(*args)
}
