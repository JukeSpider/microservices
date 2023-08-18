package com.juke.profileservice.core.data.repository

import com.juke.profileservice.core.data.entity.PasswordEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.r2dbc.repository.Modifying
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface PasswordRepository : CoroutineCrudRepository<PasswordEntity, UUID> {

    @Query("SELECT * FROM passwords WHERE user_id = :userId AND status = 'ACTIVE'")
    suspend fun findActivePasswordByUser(userId: UUID): PasswordEntity?

    @Modifying
    @Query("UPDATE passwords " +
            "SET status = 'EXPIRED', updated_at = CURRENT_TIMESTAMP " +
            "WHERE user_id = :userId")
    suspend fun revokeAllUserPasswords(userId: UUID)

    fun findAllByUserId(userId: UUID): Flow<PasswordEntity>
}