package com.juke.auth.core.data.service

import com.juke.auth.core.data.entity.UserEntity
import com.juke.auth.core.data.repository.UserRepository
import com.juke.auth.core.domain.behavior.UserBehavior
import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.features.authentication.domain.failure.EmailNotFoundFailure
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class UserService(
    private val repo: UserRepository,
    private val logger: Logger,
) : UserBehavior {

    override suspend fun findByEmail(email: String): Data<UserEntity> {
        return try {
            when (val result = repo.findByEmail(email)) {
                is UserEntity -> Success(result)
                else -> Error(EmailNotFoundFailure())
            }
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }
}