package com.juke.auth.core.data.repository

import com.juke.auth.core.data.entity.PasswordEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface PasswordRepository : CoroutineCrudRepository<PasswordEntity, UUID> {

    suspend fun findByUserId(userId: UUID): PasswordEntity?
}