package com.juke.notification.core.data.repository

import com.juke.notification.core.data.entity.TokenEntity
import com.juke.notification.core.data.entity.enums.TokenTypeEnum
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TokenRepository : CoroutineCrudRepository<TokenEntity, UUID> {

    suspend fun findByTokenAndType(token: String, type: TokenTypeEnum): TokenEntity?

    suspend fun deleteAllByUserId(userId: UUID)
}