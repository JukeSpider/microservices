package com.juke.profileservice.core.domain.behavior

import com.juke.profileservice.core.data.entity.ProfileEntity
import com.juke.profileservice.core.domain.model.Data
import java.util.*

interface ProfileBehavior {

    suspend fun findByUserId(userId: UUID): Data<ProfileEntity>

    suspend fun save(profile: ProfileEntity): Data<ProfileEntity>
}