package com.juke.profileservice.core.data.repository

import com.juke.profileservice.core.data.entity.UserEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CoroutineCrudRepository<UserEntity, UUID> {

    suspend fun findByEmail(email: String): UserEntity?
}