package com.juke.profileservice.feature.contact.domain.usecase

import com.juke.profileservice.core.domain.behavior.ProfileBehavior
import com.juke.profileservice.core.domain.model.Data
import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.core.domain.usecase.UseCase
import com.juke.profileservice.feature.contact.domain.usecase.ContactPageUseCase.GetPageParams
import com.juke.profileservice.feature.contact.domain.utils.ContactMapper
import com.juke.profileservice.feature.contact.rest.dto.ContactResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class ContactPageUseCase(
    private val profileService: ProfileBehavior,
    private val contactMapper: ContactMapper,
) : UseCase<GetPageParams, List<ContactResponse>> {

    data class GetPageParams(
        val page: Int,
        val size: Int,
        val sort: String,
    )

    override suspend fun invoke(params: GetPageParams): Data<List<ContactResponse>> {
        val (page, size, sort) = params

        val sortBy = Sort.by(sort)
        val pageable = PageRequest.of(page, size, sortBy)

        val profilesData = profileService.findByPage(pageable)
        if (profilesData is Error) return Error(profilesData.failure)

        val profiles = profilesData.value!!
        val contacts = profiles.map(contactMapper::convertToContact)

        return Success(contacts)
    }
}