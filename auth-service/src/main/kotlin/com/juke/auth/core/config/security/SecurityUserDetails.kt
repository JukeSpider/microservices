package com.juke.auth.core.config.security

import com.juke.auth.core.data.entity.PasswordEntity
import com.juke.auth.core.data.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class SecurityUserDetails(
    val user: UserEntity,
    val password: PasswordEntity,
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(user.role.toString()))
    }

    override fun getPassword() = password.pwd

    override fun getUsername() = user.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}