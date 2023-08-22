package com.juke.profileservice.core.data.repository

import com.juke.profileservice.core.data.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProfileRepository : CoroutineCrudRepository<ProfileEntity, UUID> {

    suspend fun findByUserId(userId: UUID): ProfileEntity?

    fun findAllBy(pageable: Pageable): Flow<ProfileEntity>
}