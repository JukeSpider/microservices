package com.juke.auth.features.registration.presentation.mapper

import com.juke.auth.core.presentation.mapper.Mapper
import com.juke.auth.features.registration.data.entity.enums.FlowTypeEnum
import com.juke.auth.features.registration.domain.usecase.EmailValidationUseCase.EmailParams
import com.juke.auth.features.registration.presentation.dto.EmailValidationRequest
import org.springframework.stereotype.Component

@Component
class EmailValidationMapper : Mapper<EmailValidationRequest, EmailParams> {

    override fun convert(request: EmailValidationRequest): EmailParams {
        return EmailParams(
            email = request.email!!,
            flow = FlowTypeEnum.convertToFlow(request.flow!!),
        )
    }
}
