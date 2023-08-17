package com.juke.profileservice.core.domain.behavior

import com.juke.profileservice.core.data.entity.UserEntity
import com.juke.profileservice.core.domain.model.Data

interface UserBehavior {

    suspend fun findByEmail(email: String): Data<UserEntity>

    suspend fun save(user: UserEntity): Data<UserEntity>
}