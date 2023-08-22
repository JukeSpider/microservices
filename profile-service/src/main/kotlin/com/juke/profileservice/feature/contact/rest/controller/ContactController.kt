package com.juke.profileservice.feature.contact.rest.controller

import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.feature.contact.domain.usecase.ContactPageUseCase
import com.juke.profileservice.feature.contact.domain.usecase.ContactPageUseCase.GetPageParams
import com.juke.profileservice.feature.contact.domain.usecase.FullContactUseCase
import com.juke.profileservice.feature.contact.rest.mapper.FullContactGetMapper
import kotlinx.coroutines.reactor.mono
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping("/api/profile/contacts")
class ContactController(
    private val contactPageUseCase: ContactPageUseCase,
    private val fullContactUseCase: FullContactUseCase,
    private val fullContactGetMapper: FullContactGetMapper,
) {

    @GetMapping
    fun getContacts(
        @RequestParam(name = "page", defaultValue = "0", required = false) page: Int,
        @RequestParam(name = "size", defaultValue = "5", required = false) size: Int,
        @RequestParam(name = "sort", defaultValue = "firstname", required = false) sort: String,
    ): Mono<ResponseEntity<Any>> {
        val params = GetPageParams(page, size, sort)
        return mono { contactPageUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }

    @GetMapping("/{userId}")
    fun getFullContact(
        @PathVariable userId: UUID,
    ): Mono<ResponseEntity<Any>> {
        val params = fullContactGetMapper.convert(userId)
        return mono { fullContactUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }
}