package com.juke.profileservice.feature.jobposition.presentation.rest

import com.juke.profileservice.core.domain.model.Data.Error
import com.juke.profileservice.core.domain.model.Data.Success
import com.juke.profileservice.core.domain.model.value
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionDeleteUseCase
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionGetAllUseCase
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionPostUseCase
import com.juke.profileservice.feature.jobposition.domain.usecase.JobPositionUpdateUseCase
import com.juke.profileservice.feature.jobposition.presentation.dto.JobPositionDeleteRequest
import com.juke.profileservice.feature.jobposition.presentation.dto.JobPositionPostRequest
import com.juke.profileservice.feature.jobposition.presentation.dto.JobPositionUpdateRequest
import com.juke.profileservice.feature.jobposition.presentation.mapper.JobPositionDeleteMapper
import com.juke.profileservice.feature.jobposition.presentation.mapper.JobPositionPostMapper
import com.juke.profileservice.feature.jobposition.presentation.mapper.JobPositionUpdateMapper
import kotlinx.coroutines.reactor.mono
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/profile/job-position")
class JobPositionController(
    private val getAllUseCase: JobPositionGetAllUseCase,
    private val postMapper: JobPositionPostMapper,
    private val postUseCase: JobPositionPostUseCase,
    private val updateMapper: JobPositionUpdateMapper,
    private val updateUseCase: JobPositionUpdateUseCase,
    private val deleteMapper: JobPositionDeleteMapper,
    private val deleteUseCase: JobPositionDeleteUseCase,
) {

    @GetMapping
    fun getAllJobPositions(): Mono<ResponseEntity<Any>> {
        return mono { getAllUseCase.invoke(Unit) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }

    @PostMapping
    fun postJobPosition(
        @RequestBody @Validated request: JobPositionPostRequest,
    ): Mono<ResponseEntity<Any>> {
        val params = postMapper.convert(request)
        return mono { postUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }

    @PutMapping
    fun updateJobPosition(
        @RequestBody @Validated request: JobPositionUpdateRequest,
    ): Mono<ResponseEntity<Any>> {
        val params = updateMapper.convert(request)
        return mono { updateUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }

    @DeleteMapping
    fun deleteJobPosition(
        @RequestBody @Validated request: JobPositionDeleteRequest,
    ): Mono<ResponseEntity<Any>> {
        val params = deleteMapper.convert(request)
        return mono { deleteUseCase.invoke(params) }.map {
            when (it) {
                is Success -> ResponseEntity.ok(it.value)
                is Error -> ResponseEntity.status(it.failure.code).body(it.failure)
            }
        }
    }
}