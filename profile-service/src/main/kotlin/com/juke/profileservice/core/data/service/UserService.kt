package com.juke.profileservice.core.data.service

import com.juke.profileservice.core.data.entity.UserEntity
import com.juke.profileservice.core.data.repository.UserRepository
import com.juke.profileservice.core.domain.behavior.UserBehavior
import com.juke.profileservice.core.domain.failure.EmailNotFoundFailure
import com.juke.profileservice.core.domain.failure.ServiceUnavailableFailure
import com.juke.profileservice.core.domain.failure.UserNotFoundFailure
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

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

    override suspend fun findById(id: UUID): Data<UserEntity> {
        return try {
            when (val result = repo.findById(id)) {
                is UserEntity -> Success(result)
                else -> Error(UserNotFoundFailure())
            }
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }
}