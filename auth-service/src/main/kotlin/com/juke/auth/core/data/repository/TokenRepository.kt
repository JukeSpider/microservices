package com.juke.auth.core.data.repository

import com.juke.auth.core.data.entity.TokenEntity
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TokenRepository : CoroutineCrudRepository<TokenEntity, UUID>