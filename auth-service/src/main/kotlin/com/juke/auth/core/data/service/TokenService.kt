package com.juke.auth.core.data.service

import com.juke.auth.core.data.entity.TokenEntity
import com.juke.auth.core.data.repository.TokenRepository
import com.juke.auth.core.domain.behavior.TokenBehavior
import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import org.slf4j.Logger
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val repo: TokenRepository,
    private val logger: Logger,
) : TokenBehavior {

    override suspend fun save(entity: TokenEntity): Data<TokenEntity> {
        return try {
            Success(repo.save(entity))
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }
}