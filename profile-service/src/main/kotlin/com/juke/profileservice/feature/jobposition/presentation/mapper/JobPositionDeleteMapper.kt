package com.juke.profileservice.feature.jobposition.presentation.mapper

import com.juke.profileservice.core.presentation.mapper.Mapper
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionDeleteUseCase.DeleteParams
import com.juke.profileservice.feature.jobposition.presentation.dto.JobPositionDeleteRequest
import org.springframework.stereotype.Component

@Component
class JobPositionDeleteMapper : Mapper<JobPositionDeleteRequest, DeleteParams> {

    override fun convert(request: JobPositionDeleteRequest): DeleteParams {
        return DeleteParams(name = request.name!!)
    }
}