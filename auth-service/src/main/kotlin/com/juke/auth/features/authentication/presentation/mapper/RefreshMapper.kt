package com.juke.auth.features.authentication.presentation.mapper

import com.juke.auth.core.presentation.mapper.Mapper
import com.juke.auth.features.authentication.domain.usecase.RefreshUseCase.RefreshParams
import com.juke.auth.features.authentication.presentation.dto.RefreshRequest
import org.springframework.stereotype.Component

@Component
class RefreshMapper : Mapper<RefreshRequest, RefreshParams> {

    override fun convert(request: RefreshRequest): RefreshParams {
        return RefreshParams(
            refresh = request.refresh!!
        )
    }
}