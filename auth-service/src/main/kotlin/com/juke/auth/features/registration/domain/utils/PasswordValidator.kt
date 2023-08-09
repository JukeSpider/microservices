package com.juke.auth.features.registration.domain.utils

import com.juke.auth.core.data.entity.PasswordEntity
import com.juke.auth.core.data.service.PasswordService
import com.juke.auth.core.domain.behavior.PasswordBehavior
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.value
import com.juke.auth.features.registration.domain.failure.InvalidPasswordFailure
import com.juke.auth.features.registration.domain.failure.PasswordAlreadyUsedFailure
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PasswordValidator(
    private val passwordService: PasswordBehavior,
    private val passwordEncoder: PasswordEncoder,
) {

    suspend fun validatePassword(password: String, userId: UUID): Data<Unit> {
        val passwordsData = passwordService.findAllUserPasswords(userId)
        if (passwordsData is Error) return Error(passwordsData.failure)

        val passwords = passwordsData.value!!

        val isUnique = passwords.all { p -> !passwordEncoder.matches(password, p.pwd) }

        return if (isUnique) Data.Success(Unit) else Error(PasswordAlreadyUsedFailure())
    }
}