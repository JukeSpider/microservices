package com.juke.profileservice.feature.contact.domain.failure

import com.juke.profileservice.core.domain.failure.Failure

data class ContactNotFoundFailure(
    override val code: Int = 400,
    override val status: String = "not_found",
    override val message: String = "contact not found"
) : Failure
