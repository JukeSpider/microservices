package com.juke.auth.core.data.entity

import com.juke.auth.core.data.entity.enums.UserRoleEnum
import com.juke.auth.core.data.entity.enums.UserRoleEnum.ROLE_USER
import com.juke.auth.core.data.entity.enums.UserStatusEnum
import com.juke.auth.core.data.entity.enums.UserStatusEnum.CREATED
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.UUID

@Table(name = "customers")
data class UserEntity(

    @Id
    val id: UUID = UUID.randomUUID(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val modifiedAt: LocalDateTime = LocalDateTime.now(),

    val email: String,
    val status: UserStatusEnum = CREATED,
    val role: UserRoleEnum = ROLE_USER,
)