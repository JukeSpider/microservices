package com.juke.auth.features.registration.domain.failure

import com.juke.auth.core.domain.failure.Failure

data class UndefinedFlowFailure(
    override val code: Int = 400,
    override val status: String = "undefined",
    override val message: String = "undefined flow"
) : Failure