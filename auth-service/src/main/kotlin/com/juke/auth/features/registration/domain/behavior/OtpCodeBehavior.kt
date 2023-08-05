package com.juke.auth.features.registration.domain.behavior

import com.juke.auth.core.data.entity.UserEntity
import com.juke.auth.core.domain.model.Data
import com.juke.auth.features.registration.data.entity.OtpCodeEntity
import java.util.UUID

interface OtpCodeBehavior {

    suspend fun save(otpCode: OtpCodeEntity): Data<OtpCodeEntity>

    suspend fun revokeAllUserOtpCodes(userId: UUID): Data<Unit>
}