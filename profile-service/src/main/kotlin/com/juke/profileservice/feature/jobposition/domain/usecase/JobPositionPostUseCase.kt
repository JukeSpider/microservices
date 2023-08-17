package com.juke.profileservice.feature.jobposition.domain.usecase

import com.juke.profileservice.core.data.entity.JobPositionEntity
import com.juke.profileservice.core.domain.behavior.JobPositionBehavior
import com.juke.profileservice.core.domain.failure.ServiceUnavailableFailure
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.success.StatusResponse
import com.juke.profileservice.core.domain.usecase.UseCase
import com.juke.profileservice.feature.jobposition.domain.failure.InvalidNameFailure
import com.juke.profileservice.feature.jobposition.domain.success.JobPositionStatusEnum.CREATED
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionPostUseCase.PostParams
import org.springframework.stereotype.Component

@Component
class JobPositionPostUseCase(
    private val jobPositionService: JobPositionBehavior,
): UseCase<PostParams, StatusResponse> {

    data class PostParams(
        val name: String
    )

    override suspend fun invoke(params: PostParams): Data<StatusResponse> {
        val jobPositionData = jobPositionService.findByNameIgnoreCase(params.name)
        if (jobPositionData is Success)
            return Error(InvalidNameFailure())
        if (jobPositionData is Error && jobPositionData.failure is ServiceUnavailableFailure)
            return Error(ServiceUnavailableFailure())

        val jobPosition = JobPositionEntity(name = params.name)

        val savedData = jobPositionService.save(jobPosition)
        if (savedData is Error) return Error(savedData.failure)

        return Success(StatusResponse(CREATED.getMessage()))
    }
}