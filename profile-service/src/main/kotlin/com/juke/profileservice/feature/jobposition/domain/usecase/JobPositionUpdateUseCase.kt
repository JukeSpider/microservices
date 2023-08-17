package com.juke.profileservice.feature.jobposition.domain.usecase

import com.juke.profileservice.core.domain.behavior.JobPositionBehavior
import com.juke.profileservice.core.domain.failure.JobPositionNotFoundFailure
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.core.domain.success.StatusResponse
import com.juke.profileservice.core.domain.usecase.UseCase
import com.juke.profileservice.feature.jobposition.domain.failure.InvalidIdFailure
import com.juke.profileservice.feature.jobposition.domain.success.JobPositionStatusEnum.UPDATED
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionUpdateUseCase.UpdateParams
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*

@Component
class JobPositionUpdateUseCase(
    private val jobPositionService: JobPositionBehavior,
): UseCase<UpdateParams, StatusResponse> {

    data class UpdateParams(
        val id: UUID,
        val name: String,
    )

    override suspend fun invoke(params: UpdateParams): Data<StatusResponse> {
        val jobPositionData = jobPositionService.findById(params.id)
        if (jobPositionData is Error) {
            return when (jobPositionData.failure) {
                is JobPositionNotFoundFailure -> Error(InvalidIdFailure())
                else -> Error(jobPositionData.failure)
            }
        }

        val jobPosition = jobPositionData.value!!

        val updated = jobPosition.copy(
            name = params.name,
            updatedAt = LocalDateTime.now()
        )

        val savedData = jobPositionService.save(updated)
        if (savedData is Error) return Error(savedData.failure)

        return Success(StatusResponse(UPDATED.getMessage()))
    }
}
