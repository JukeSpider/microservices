package com.juke.auth.core.domain.behavior

import com.juke.auth.core.data.entity.PasswordEntity
import com.juke.auth.core.domain.model.Data
import java.util.*

interface PasswordBehavior {

    suspend fun findActiveUserPassword(userId: UUID): Data<PasswordEntity>

    suspend fun save(password: PasswordEntity): Data<PasswordEntity>

    suspend fun revokeAllUserPasswords(userId: UUID): Data<Unit>

    suspend fun findAllUserPasswords(userId: UUID): Data<List<PasswordEntity>>
}