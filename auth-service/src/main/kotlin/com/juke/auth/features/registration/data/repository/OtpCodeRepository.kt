package com.juke.auth.features.registration.data.repository

import com.juke.auth.features.registration.data.entity.OtpCodeEntity
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OtpCodeRepository : CoroutineCrudRepository<OtpCodeEntity, UUID> {

    @Modifying
    @Query("UPDATE OTP_CODES " +
            "SET STATUS = 'REVOKED', UPDATED_AT = CURRENT_TIMESTAMP " +
            "WHERE USER_ID = :userId")
    suspend fun revokeAllUserOtpCodes(userId: UUID)
}