package com.juke.auth.features.registration.presentation.mapper

import com.juke.auth.core.presentation.mapper.Mapper
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import com.juke.auth.features.registration.domain.usecase.CodeValidationUseCase.CodeParams
import com.juke.auth.features.registration.presentation.dto.CodeValidationRequest
import org.springframework.stereotype.Component

@Component
class CodeValidationMapper : Mapper<CodeValidationRequest, CodeParams> {

    override fun convert(request: CodeValidationRequest): CodeParams {
        return CodeParams(
            email = request.email!!,
            code = request.code!!,
            flow = FlowTypeEnum.convertToFlow(request.flow!!),
        )
    }
}
