package com.juke.auth.core.data.service

import com.juke.auth.core.data.entity.PasswordEntity
import com.juke.auth.core.data.repository.PasswordRepository
import com.juke.auth.core.domain.behavior.PasswordBehavior
import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.features.authentication.domain.failure.PasswordNotFoundFailure
import kotlinx.coroutines.flow.toList
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class PasswordService(
    private val repo: PasswordRepository,
    private val logger: Logger,
) : PasswordBehavior {

    override suspend fun findActiveUserPassword(userId: UUID): Data<PasswordEntity> {
        return try {
            when (val result = repo.findActivePasswordByUser(userId)) {
                is PasswordEntity -> Success(result)
                else -> Error(PasswordNotFoundFailure())
            }
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }

    @Transactional
    override suspend fun save(password: PasswordEntity): Data<PasswordEntity> {
        return try {
            Success(repo.save(password))
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }

    @Transactional
    override suspend fun revokeAllUserPasswords(userId: UUID): Data<Unit> {
        return try {
            Success(repo.revokeAllUserPasswords(userId))
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }

    override suspend fun findAllUserPasswords(userId: UUID): Data<List<PasswordEntity>> {
        return try {
            Success(repo.findAllByUserId(userId).toList())
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }
}