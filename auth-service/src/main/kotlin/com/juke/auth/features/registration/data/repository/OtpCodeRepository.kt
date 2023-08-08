package com.juke.auth.features.registration.data.repository

import com.juke.auth.features.registration.data.entity.OtpCodeEntity
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OtpCodeRepository : CoroutineCrudRepository<OtpCodeEntity, UUID> {

    @Modifying
    @Query("UPDATE otp_codes " +
            "SET status = 'REVOKED', updated_at = CURRENT_TIMESTAMP " +
            "WHERE user_id = :userId")
    suspend fun revokeAllUserOtpCodes(userId: UUID)

    suspend fun findByCodeAndUserIdAndFlow(code: String, userId: UUID, flow: FlowTypeEnum): OtpCodeEntity?
}