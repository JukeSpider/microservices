package com.juke.profileservice.feature.profile.domain.failure

import com.juke.profileservice.core.domain.failure.Failure

data class ProfileNotAvailableFailure(
    override val code: Int = 403,
    override val status: String = "not_available",
    override val message: String = "profile not available",
) : Failure