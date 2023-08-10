package com.juke.notification.core.data.service

import com.juke.notification.core.data.entity.UserEntity
import com.juke.notification.core.data.repository.UserRepository
import com.juke.notification.core.domain.behavior.UserBehavior
import com.juke.notification.core.domain.failure.EmailNotFoundFailure
import com.juke.notification.core.domain.failure.ServiceUnavailableFailure
import com.juke.notification.core.domain.model.Data
import com.juke.notification.core.domain.model.Data.Error
import com.juke.notification.core.domain.model.Data.Success
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Transactional
    override suspend fun save(user: UserEntity): Data<UserEntity> {
        return try {
            Success(repo.save(user))
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }
}