package com.juke.profileservice.core.data.entity

import com.juke.profileservice.core.data.entity.enums.UserRoleEnum
import com.juke.profileservice.core.data.entity.enums.UserRoleEnum.ROLE_USER
import com.juke.profileservice.core.data.entity.enums.UserStatusEnum
import com.juke.profileservice.core.data.entity.enums.UserStatusEnum.CREATED
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table(name = "users")
data class UserEntity(

    @Id
    val id: UUID? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),

    val email: String,
    val status: UserStatusEnum = CREATED,
    val role: UserRoleEnum = ROLE_USER,
)