package com.juke.notification.feature.mail.presentation.mapper

import com.juke.notification.core.presentation.mapper.Mapper
import com.juke.notification.feature.mail.domain.usecase.OtpMailUseCase.OtpMailParams
import com.juke.notification.feature.mail.presentation.dto.OtpMailRequest
import org.springframework.stereotype.Component

@Component
class OtpMailMapper : Mapper<OtpMailRequest, OtpMailParams> {

    override fun convert(request: OtpMailRequest): OtpMailParams {
        return OtpMailParams(code = request.code!!, email = request.email!!)
    }
}