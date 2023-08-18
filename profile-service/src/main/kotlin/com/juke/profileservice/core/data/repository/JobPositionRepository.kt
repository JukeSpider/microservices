package com.juke.profileservice.core.data.repository

import com.juke.profileservice.core.data.entity.JobPositionEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface JobPositionRepository : CoroutineCrudRepository<JobPositionEntity, UUID> {

    suspend fun findByNameIgnoreCase(name: String): JobPositionEntity?

    suspend fun deleteByName(name: String)
}