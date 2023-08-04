package com.juke.auth.features.registration.data.service

import com.juke.auth.core.data.entity.UserEntity
import com.juke.auth.core.domain.failure.ServiceUnavailableFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.features.registration.data.entity.OtpCodeEntity
import com.juke.auth.features.registration.data.repository.OtpCodeRepository
import com.juke.auth.features.registration.domain.behavior.OtpCodeBehavior
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class OtpCodeService(
    private val repo: OtpCodeRepository,
    private val logger: Logger,
): OtpCodeBehavior {

    @Transactional
    override suspend fun save(otpCode: OtpCodeEntity): Data<OtpCodeEntity> {
        return try {
            Data.Success(repo.save(otpCode))
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Data.Error(ServiceUnavailableFailure())
        }
    }

    @Transactional
    override suspend fun revokeAllUserOtpCodes(userId: UUID): Data<Unit> {
        return try {
            repo.revokeAllUserOtpCodes(userId)
            Data.Success(Unit)
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Data.Error(ServiceUnavailableFailure())
        }
    }

}