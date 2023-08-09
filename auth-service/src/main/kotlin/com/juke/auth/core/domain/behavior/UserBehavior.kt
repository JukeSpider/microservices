package com.juke.auth.core.domain.behavior

import com.juke.auth.core.data.entity.UserEntity
import com.juke.auth.core.domain.model.Data
import java.util.UUID

interface UserBehavior {

    suspend fun findByEmail(email: String): Data<UserEntity>

    suspend fun save(user: UserEntity): Data<UserEntity>
}