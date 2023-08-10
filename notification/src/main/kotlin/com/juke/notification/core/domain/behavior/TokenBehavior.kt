package com.juke.notification.core.domain.behavior

import com.juke.notification.core.data.entity.TokenEntity
import com.juke.notification.core.data.entity.enums.TokenTypeEnum
import com.juke.notification.core.domain.model.Data
import java.util.*

interface TokenBehavior {

    suspend fun save(entity: TokenEntity): Data<TokenEntity>

    suspend fun deleteById(id: UUID): Data<Unit>

    suspend fun findByToken(token: String, type: TokenTypeEnum): Data<TokenEntity>

    suspend fun deleteAllUserTokens(userId: UUID): Data<Unit>
}