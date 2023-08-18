package com.juke.profileservice.feature.jobposition.domain.usecase

import com.juke.profileservice.core.data.service.JobPositionService
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.core.domain.usecase.UseCase
import com.juke.profileservice.feature.jobposition.presentation.dto.JobPositionResponse
import org.springframework.stereotype.Component

@Component
class JobPositionGetAllUseCase(
    private val jobPositionService: JobPositionService,
): UseCase<Unit, List<JobPositionResponse>> {

    override suspend fun invoke(params: Unit): Data<List<JobPositionResponse>> {
        val listData = jobPositionService.findAll()
        if (listData is Error) return Error(listData.failure)

        val jobPositions = listData.value!!

        val response = jobPositions.map {
            jobPosition -> JobPositionResponse(id = jobPosition.id!!, name = jobPosition.name)
        }

        return Success(response)
    }
}