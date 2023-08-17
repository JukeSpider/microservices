package com.juke.profileservice.feature.jobposition.presentation.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class JobPositionDeleteRequest(

    @NotNull
    @NotBlank
    val name: String?
)