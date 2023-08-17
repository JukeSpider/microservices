package com.juke.profileservice.feature.jobposition.presentation.mapper

import com.juke.profileservice.core.presentation.mapper.Mapper
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionUpdateUseCase.UpdateParams
import com.juke.profileservice.feature.jobposition.presentation.dto.JobPositionUpdateRequest
import org.springframework.stereotype.Component

@Component
class JobPositionUpdateMapper: Mapper<JobPositionUpdateRequest, UpdateParams> {

    override fun convert(request: JobPositionUpdateRequest): UpdateParams {
        return UpdateParams(request.id!!, request.name!!)
    }
}
