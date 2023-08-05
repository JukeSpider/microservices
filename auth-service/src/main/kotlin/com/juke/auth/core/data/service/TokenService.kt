package com.juke.auth.core.data.service

import com.juke.auth.core.data.entity.TokenEntity
import com.juke.auth.core.data.entity.enums.TokenTypeEnum
import com.juke.auth.core.data.repository.TokenRepository
import com.juke.auth.core.domain.behavior.TokenBehavior
import com.juke.auth.core.domain.failure.InvalidTokenFailure
import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class TokenService(
    private val repo: TokenRepository,
    private val logger: Logger,
) : TokenBehavior {

    @Transactional
    override suspend fun save(entity: TokenEntity): Data<TokenEntity> {
        return try {
            Success(repo.save(entity))
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }

    @Transactional
    override suspend fun deleteById(id: UUID): Data<Unit> {
        return try {
            repo.deleteById(id)
            Success(Unit)
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }

    override suspend fun findByToken(
        token: String,
        type: TokenTypeEnum
    ): Data<TokenEntity> {
        return try {
            when (val result = repo.findByTokenAndType(token, type)) {
                is TokenEntity -> Success(result)
                else -> Error(InvalidTokenFailure())
            }
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }
}