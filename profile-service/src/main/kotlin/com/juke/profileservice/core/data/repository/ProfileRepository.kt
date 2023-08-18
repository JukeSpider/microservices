package com.juke.profileservice.core.data.repository

import com.juke.profileservice.core.data.entity.ProfileEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfileRepository : CoroutineCrudRepository<ProfileEntity, UUID> {

    suspend fun findByUserId(userId: UUID): ProfileEntity?
}