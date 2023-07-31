package com.juke.auth.core.data.repository

import com.juke.auth.core.data.entity.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : CoroutineCrudRepository<UserEntity, UUID> {

    suspend fun findByEmail(email: String): UserEntity?
}