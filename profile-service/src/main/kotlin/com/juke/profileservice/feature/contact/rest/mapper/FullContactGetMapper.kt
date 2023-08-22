package com.juke.profileservice.feature.contact.rest.mapper

import com.juke.profileservice.core.presentation.mapper.Mapper
import com.juke.profileservice.feature.contact.domain.usecase.FullContactUseCase.FullContactParams
import org.springframework.stereotype.Component
import java.util.*

@Component
class FullContactGetMapper : Mapper<UUID, FullContactParams> {

    override fun convert(request: UUID): FullContactParams {
        return FullContactParams(request)
    }
}