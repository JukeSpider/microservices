package com.juke.profileservice.core.data.service

import com.juke.profileservice.core.data.entity.JobPositionEntity
import com.juke.profileservice.core.data.repository.JobPositionRepository
import com.juke.profileservice.core.domain.behavior.JobPositionBehavior
import com.juke.profileservice.core.domain.failure.JobPositionNotFoundFailure
import com.juke.profileservice.core.domain.failure.ServiceUnavailableFailure
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import kotlinx.coroutines.flow.toList
import org.slf4j.Logger
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class JobPositionService(
    private val repo: JobPositionRepository,
    private val logger: Logger,
) : JobPositionBehavior {

    override suspend fun findAll(): Data<List<JobPositionEntity>> {
        return try {
            Success(repo.findAll().toList())
        } catch (t: Throwable) {
            logger.error("Unexpected error", t)
            Error(ServiceUnavailableFailure())
        }
    }

    override suspend fun findById(id: UUID): Data<JobPositionEntity> {
        return try {
            return when (val jobPosition = repo.findById(id)) {
                null -> Error(JobPositionNotFoundFailure())
                else -> Success(jobPosition)
            }
        } catch (t: Throwable) {
            logger.error("Unexpected error", t)
            Error(ServiceUnavailableFailure())
        }
    }

    override suspend fun findByNameIgnoreCase(name: String): Data<JobPositionEntity> {
        return try {
            return when (val jobPosition = repo.findByNameIgnoreCase(name)) {
                null -> Error(JobPositionNotFoundFailure())
                else -> Success(jobPosition)
            }
        } catch (t: Throwable) {
            logger.error("Unexpected error", t)
            Error(ServiceUnavailableFailure())
        }
    }

    @Transactional
    override suspend fun save(jobPosition: JobPositionEntity): Data<JobPositionEntity> {
        return try {
            Success(repo.save(jobPosition))
        } catch (t: Throwable) {
            logger.error("Unexpected error", t)
            Error(ServiceUnavailableFailure())
        }
    }

    @Transactional
    override suspend fun delete(jobPosition: JobPositionEntity): Data<Unit> {
        return try {
            Success(repo.delete(jobPosition))
        } catch (t: Throwable) {
            logger.error("Unexpected error", t)
            Error(ServiceUnavailableFailure())
        }
    }
}