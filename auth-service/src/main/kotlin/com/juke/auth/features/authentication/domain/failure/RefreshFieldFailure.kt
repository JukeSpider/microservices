package com.juke.auth.features.authentication.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class RefreshFieldFailure(
    override val code: Int = 400,
    override val status: String = "refresh_token_is_missed",
    override val message: String = "Field <refresh> is missed!"
) : Failure
