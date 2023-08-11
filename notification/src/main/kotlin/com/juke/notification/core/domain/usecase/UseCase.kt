package com.juke.notification.core.domain.usecase

import com.juke.notification.core.domain.model.Data

interface UseCase<in Params : Any, Result : Any> {
    suspend fun invoke(params: Params): Data<Result>
}