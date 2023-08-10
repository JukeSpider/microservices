package com.juke.notification.core.config.security

import com.juke.notification.core.data.service.PasswordService
import com.juke.notification.core.data.service.UserService
import com.juke.notification.core.domain.failure.EmailNotFoundFailure
import com.juke.notification.core.domain.failure.PasswordNotFoundFailure
import com.juke.notification.core.domain.model.Data
import com.juke.notification.core.domain.model.Data.Error
import com.juke.notification.core.domain.model.value
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class SecurityUserDetailsService(
    private val userService: UserService,
    private val passwordService: PasswordService,
) : ReactiveUserDetailsService {

    override fun findByUsername(username: String): Mono<UserDetails> {
        return mono { help(username).value }
            .handle { t, u ->
                run {
                    if (t !is UserDetails) {
                        u.error(UsernameNotFoundException(""))
                    } else {
                        u.next(t)
                    }
                }
            }
    }

    private suspend fun help(email: String): Data<UserDetails> {
        val userData = userService.findByEmail(email)
        if (userData is Error) return Error(EmailNotFoundFailure())

        val user = userData.value!!

        val passwordData = passwordService.findActiveUserPassword(user.id!!)
        if (passwordData is Error) return Error(PasswordNotFoundFailure())

        val password = passwordData.value!!

        return Data.Success(SecurityUserDetails(user, password))
    }
}