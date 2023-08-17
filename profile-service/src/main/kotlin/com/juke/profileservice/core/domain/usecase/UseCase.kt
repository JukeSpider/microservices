package com.juke.profileservice.core.domain.usecase

import com.juke.profileservice.core.domain.model.Data

interface UseCase<in Params : Any, Result : Any> {
    suspend fun invoke(params: Params): Data<Result>
}