package com.juke.auth.features.registration.data.service

import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import com.juke.auth.features.registration.data.entity.OtpCodeEntity
import com.juke.auth.features.registration.data.repository.OtpCodeRepository
import com.juke.auth.features.registration.domain.behavior.OtpCodeBehavior
import com.juke.auth.features.registration.domain.failure.OtpNotFoundFailure
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OtpCodeService(
    private val repo: OtpCodeRepository,
    private val logger: Logger,
): OtpCodeBehavior {

    @Transactional
    override suspend fun save(otpCode: OtpCodeEntity): Data<OtpCodeEntity> {
        return try {
            Success(repo.save(otpCode))
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }

    @Transactional
    override suspend fun revokeAllUserOtpCodes(userId: UUID): Data<Unit> {
        return try {
            repo.revokeAllUserOtpCodes(userId)
            Success(Unit)
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }

    override suspend fun findByCodeAndUser(code: String, userId: UUID): Data<OtpCodeEntity> {
        return try {
            when (val otpCode = repo.findByCodeAndUserId(code, userId)) {
                is OtpCodeEntity -> Success(otpCode)
                else -> Error(OtpNotFoundFailure())
            }
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }

}