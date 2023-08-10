package com.juke.notification.core.domain.behavior

import com.juke.notification.core.data.entity.UserEntity
import com.juke.notification.core.domain.model.Data

interface UserBehavior {

    suspend fun findByEmail(email: String): Data<UserEntity>

    suspend fun save(user: UserEntity): Data<UserEntity>
}