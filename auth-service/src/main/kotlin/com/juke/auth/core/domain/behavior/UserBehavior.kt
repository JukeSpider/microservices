package com.juke.auth.core.domain.behavior

import com.juke.auth.core.data.entity.UserEntity
import com.juke.auth.core.domain.model.Data

interface UserBehavior {

    suspend fun findByEmail(email: String): Data<UserEntity>
}