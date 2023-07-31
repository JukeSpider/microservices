package com.juke.auth.core.domain.usecase

import com.juke.auth.core.domain.model.Data

interface UseCase<in Params : Any, Result : Any> {
    suspend fun invoke(params: Params): Data<Result>
}