package com.juke.profileservice.core.domain.behavior

import com.juke.profileservice.core.data.entity.JobPositionEntity
import com.juke.profileservice.core.domain.model.Data
import java.util.*

interface JobPositionBehavior {

    suspend fun findAll(): Data<List<JobPositionEntity>>

    suspend fun findById(id: UUID): Data<JobPositionEntity>

    suspend fun findByNameIgnoreCase(name: String): Data<JobPositionEntity>

    suspend fun save(jobPosition: JobPositionEntity): Data<JobPositionEntity>

    suspend fun delete(jobPosition: JobPositionEntity): Data<Unit>
}