package com.juke.profileservice.feature.jobposition.domain.failure

import com.juke.profileservice.core.domain.failure.Failure

data class InvalidNameFailure(
    override val code: Int = 403,
    override val status: String = "invalid_name",
    override val message: String = "invalid name",
) : Failure