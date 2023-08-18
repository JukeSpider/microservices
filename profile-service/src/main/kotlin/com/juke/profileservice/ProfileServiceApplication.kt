package com.juke.profileservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@ConfigurationPropertiesScan
@EnableR2dbcRepositories
@EnableDiscoveryClient
@SpringBootApplication
class ProfileServiceApplication

fun main(args: Array<String>) {
	runApplication<ProfileServiceApplication>(*args)
}
