package com.juke.auth.features.registration.domain.utils

import com.juke.auth.core.data.entity.UserEntity
import com.juke.auth.core.data.entity.enums.UserStatusEnum
import com.juke.auth.core.data.entity.enums.UserStatusEnum.*
import com.juke.auth.core.data.service.UserService
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.core.domain.model.value
import com.juke.auth.features.authentication.domain.failure.EmailNotFoundFailure
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum.*
import com.juke.auth.features.registration.domain.failure.PasswordResetUnavailableFailure
import com.juke.auth.features.registration.domain.failure.RegistrationUnavailableFailure
import com.juke.auth.features.registration.domain.failure.UndefinedFlowFailure
import org.springframework.stereotype.Component

@Component
class EmailValidator(
    private val userService: UserService,
) {

    suspend fun validateEmail(email: String, flow: FlowTypeEnum): Data<UserEntity> {
        if (flow == UNDEFINED) return Error(UndefinedFlowFailure())

        val userData = userService.findByEmail(email)

        val failure =
            if (flow == REGISTRATION)   RegistrationUnavailableFailure()
            else                        PasswordResetUnavailableFailure()

        if (userData is Error) {
            return when (userData.failure) {
                is EmailNotFoundFailure -> Error(failure)
                else -> Error(userData.failure)
            }
        }

        val user = userData.value!!
        if (!checkStatus(user.status, flow)) return Error(failure)

        return Success(user)
    }

    private fun checkStatus(status: UserStatusEnum, flow: FlowTypeEnum): Boolean {
        return when (flow) {
            REGISTRATION -> status == CREATED
            else -> status == VERIFIED || status == ACTIVE
        }
    }
}