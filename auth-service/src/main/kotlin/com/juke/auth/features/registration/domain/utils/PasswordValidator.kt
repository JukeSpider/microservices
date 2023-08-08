package com.juke.auth.features.registration.domain.utils

import org.springframework.stereotype.Component

@Component
class PasswordValidator {

    private val specials = ".!@#$%^&*()-_<>"

    fun validate(password: String) : Boolean {
        if (password.length < 8) return false

        val hasUpper = password.chars().anyMatch(Character::isUpperCase)
        val hasLower = password.chars().anyMatch(Character::isLowerCase)
        val hasDigit = password.chars().anyMatch(Character::isDigit)
        val hasSpecial = password.chars().anyMatch{ c -> specials.contains(c.toChar()) }
        val onlyAllowed = password.chars().allMatch(this::isAllowed)

        return hasUpper && hasLower && hasDigit && hasSpecial && onlyAllowed
    }

    private fun isAllowed(char: Int) : Boolean {
        val c = char.toChar()

        val isUpper = c.isUpperCase()
        val isLower = c.isLowerCase()
        val isDigit = c.isDigit()
        val isSpecial = specials.contains(c)

        return isUpper || isLower || isDigit || isSpecial
    }
}