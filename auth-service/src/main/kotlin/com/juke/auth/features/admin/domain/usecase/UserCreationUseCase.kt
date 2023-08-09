package com.juke.auth.features.admin.domain.usecase

import com.juke.auth.core.data.entity.UserEntity
import com.juke.auth.core.domain.behavior.UserBehavior
import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.success.StatusResponse
import com.juke.auth.core.domain.usecase.UseCase
import com.juke.auth.features.admin.domain.failure.UserAlreadyExistsFailure
import com.juke.auth.features.admin.domain.success.StatusEnum
import com.juke.auth.features.admin.domain.usecase.UserCreationUseCase.UserCreationParams
import org.springframework.stereotype.Component

@Component
class UserCreationUseCase(
    private val userService: UserBehavior,
) : UseCase<UserCreationParams, StatusResponse> {

    data class UserCreationParams(
        val email: String
    )

    override suspend fun invoke(params: UserCreationParams): Data<StatusResponse> {
        when (val userData = userService.findByEmail(params.email)) {
            is Success -> return Error(UserAlreadyExistsFailure())
            is Error -> if (userData.failure is ServiceUnavailableFailure) {
                return Error(ServiceUnavailableFailure())
            }
        }

        val user = UserEntity(email = params.email)

        val userSavedData = userService.save(user)
        if (userSavedData is Error) return Error(userSavedData.failure)

        return Success(StatusResponse(StatusEnum.CREATED.getMessage()))
    }
}