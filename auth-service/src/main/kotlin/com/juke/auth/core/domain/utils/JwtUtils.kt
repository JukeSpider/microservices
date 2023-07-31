package com.juke.auth.core.domain.utils

import com.juke.auth.core.config.properties.JwtProperties
import com.juke.auth.core.data.entity.enums.TokenTypeEnum
import com.juke.auth.core.data.entity.enums.TokenTypeEnum.ACCESS
import com.juke.auth.core.data.entity.enums.TokenTypeEnum.REFRESH
import com.juke.auth.core.data.entity.enums.UserRoleEnum
import com.juke.auth.core.domain.failure.ExpiredTokenFailure
import com.juke.auth.core.domain.failure.InvalidTokenFailure
import com.juke.auth.core.domain.model.Data
import com.juke.auth.core.domain.model.Data.Error
import com.juke.auth.core.domain.model.Data.Success
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.Logger
import org.springframework.stereotype.Component
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Component
class JwtUtils(
    private val config: JwtProperties,
    private val logger: Logger,
) {

    data class TokenCredentials(
        val email: String,
        val role: UserRoleEnum,
    )

    fun getToken(email: String, role: UserRoleEnum, type: TokenTypeEnum): String {

        val claims = Jwts.claims().setSubject(email)
        claims["role"] = role.toString()

        val issue = LocalDateTime.now()
        val expired = when (type) {
            ACCESS -> issue.plusMinutes(config.access)
            REFRESH -> issue.plusMonths(config.refresh)
        }

        return Jwts.builder().setClaims(claims)
            .setSubject(email)
            .setIssuedAt(
                Date.from(issue.atZone(ZoneId.systemDefault()).toInstant())
            )
            .setExpiration(
                Date.from(expired.atZone(ZoneId.systemDefault()).toInstant())
            )
            .signWith(getKeyFromSecret(config.secret))
            .compact()
    }

    fun getCredentials(token: String): Data<TokenCredentials> {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(getKeyFromSecret(config.secret))
                .build()
                .parseClaimsJws(token)
                .body

            val email = claims.subject
            val role = claims["role"] as String
            val roleEnum = UserRoleEnum.valueOf(role)

            return Success(TokenCredentials(email, roleEnum))

        } catch (e: ExpiredJwtException) {
            logger.error("Token is expired!", e)
            Error(ExpiredTokenFailure())
        } catch (e: Exception) {
            logger.error("Token is invalid!", e)
            Error(InvalidTokenFailure())
        }
    }

    private fun getKeyFromSecret(secret: String): Key {
        val secretBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(secretBytes)
    }
}