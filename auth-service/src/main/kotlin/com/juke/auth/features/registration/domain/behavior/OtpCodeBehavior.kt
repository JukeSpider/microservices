package com.juke.auth.features.registration.domain.behavior

import com.juke.auth.core.domain.model.Data
import com.juke.auth.features.registration.data.entity.OtpCodeEntity
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import java.util.*

interface OtpCodeBehavior {

    suspend fun save(otpCode: OtpCodeEntity): Data<OtpCodeEntity>

    suspend fun revokeAllUserOtpCodes(userId: UUID): Data<Unit>

    suspend fun findByCodeUserFlow(code: String, userId: UUID, flow: FlowTypeEnum): Data<OtpCodeEntity>
}