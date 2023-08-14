package com.juke.notification.core.domain.failure

data class ServiceUnavailableFailure(
    override val code: Int = 500,
    override val status: String = "unavailable_auth_service",
    override val message: String = "Service is Unavailable!"
) : Failure