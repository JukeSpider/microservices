package com.juke.auth.core.domain.behavior

import com.juke.auth.core.data.entity.TokenEntity
import com.juke.auth.core.domain.model.Data

interface TokenBehavior {

    suspend fun save(entity: TokenEntity): Data<TokenEntity>

    suspend fun findByToken(token: String): Data<TokenEntity>
}