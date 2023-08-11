package com.juke.notification.core.data.repository

import com.juke.notification.core.data.entity.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CoroutineCrudRepository<UserEntity, UUID> {

    suspend fun findByEmail(email: String): UserEntity?
}