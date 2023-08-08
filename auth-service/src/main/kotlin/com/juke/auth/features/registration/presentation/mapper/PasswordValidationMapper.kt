package com.juke.auth.features.registration.presentation.mapper

import com.juke.auth.core.presentation.mapper.Mapper
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import com.juke.auth.features.registration.domain.usecase.PasswordValidationUseCase.PasswordParams
import com.juke.auth.features.registration.presentation.dto.PasswordValidationRequest
import org.springframework.stereotype.Component

@Component
class PasswordValidationMapper : Mapper<PasswordValidationRequest, PasswordParams> {

    override fun convert(request: PasswordValidationRequest): PasswordParams {
        return PasswordParams(
            email = request.email!!,
            code = request.code!!,
            password = request.password!!,
            flow = FlowTypeEnum.convertToFlow(request.flow!!),
        )
    }
}
