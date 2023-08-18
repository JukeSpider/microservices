package com.juke.profileservice.feature.jobposition.presentation.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class JobPositionUpdateRequest(

    @NotNull
    val id: UUID?,

    @NotNull
    @NotBlank
    val name: String?
)
