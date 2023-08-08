package com.juke.auth.core.domain.behavior

import com.juke.auth.core.data.entity.PasswordEntity
import com.juke.auth.core.domain.model.Data
import java.util.*

interface PasswordBehavior {

    suspend fun findByUserId(userId: UUID): Data<PasswordEntity>

    suspend fun save(password: PasswordEntity): Data<PasswordEntity>
}