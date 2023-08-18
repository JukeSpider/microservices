package com.juke.profileservice.feature.jobposition.domain.usecase

import com.juke.profileservice.core.domain.behavior.JobPositionBehavior
import com.juke.profileservice.core.domain.failure.JobPositionNotFoundFailure
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.core.domain.success.StatusResponse
import com.juke.profileservice.core.domain.usecase.UseCase
import com.juke.profileservice.feature.jobposition.domain.failure.InvalidNameFailure
import com.juke.profileservice.feature.jobposition.domain.success.JobPositionStatusEnum.DELETED
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionDeleteUseCase.DeleteParams
import org.springframework.stereotype.Component

@Component
class JobPositionDeleteUseCase(
    private val jobPositionService: JobPositionBehavior
): UseCase<DeleteParams, StatusResponse> {
    data class DeleteParams(
        val name: String
    )

    override suspend fun invoke(params: DeleteParams): Data<StatusResponse> {
        val jobPositionData = jobPositionService.findByNameIgnoreCase(params.name)
        if (jobPositionData is Error) {
            return when (jobPositionData.failure) {
                is JobPositionNotFoundFailure -> Error(InvalidNameFailure())
                else -> Error(jobPositionData.failure)
            }
        }

        val jobPosition = jobPositionData.value!!

        val deletedData = jobPositionService.delete(jobPosition)
        if (deletedData is Error) return Error(deletedData.failure)

        return Success(StatusResponse(DELETED.getMessage()))
    }
}