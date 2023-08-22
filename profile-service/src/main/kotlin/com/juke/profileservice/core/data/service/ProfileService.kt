package com.juke.profileservice.core.data.service

import com.juke.profileservice.core.data.entity.ProfileEntity
import com.juke.profileservice.core.data.repository.ProfileRepository
import com.juke.profileservice.core.domain.behavior.ProfileBehavior
import com.juke.profileservice.core.domain.failure.ProfileNotFoundFailure
import com.juke.profileservice.core.domain.failure.ServiceUnavailableFailure
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProfileService(
    private val repo: ProfileRepository,
    private val logger: Logger,
) : ProfileBehavior {

    override suspend fun findByUserId(userId: UUID): Data<ProfileEntity> {
        return try {
            when (val result = repo.findByUserId(userId)) {
                is ProfileEntity -> Success(result)
                else -> Error(ProfileNotFoundFailure())
            }
        } catch (t: Throwable) {
            logger.error("Unexpected exception", t)
            Error(ServiceUnavailableFailure())
        }
    }

    override suspend fun save(profile: ProfileEntity): Data<ProfileEntity> {
        return try {
            Success(repo.save(profile))
        } catch (t: Throwable) {
            logger.error("Unexpected exception", t)
            Error(ServiceUnavailableFailure())
        }
    }
}