package com.juke.profileservice.feature.jobposition.presentation.mapper

import com.juke.profileservice.core.presentation.mapper.Mapper
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionPostUseCase.PostParams
import com.juke.profileservice.feature.jobposition.presentation.dto.JobPositionPostRequest
import org.springframework.stereotype.Component

@Component
class JobPositionPostMapper: Mapper<JobPositionPostRequest, PostParams> {

    override fun convert(request: JobPositionPostRequest): PostParams {
        return PostParams(name = request.name!!)
    }
}