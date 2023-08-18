package com.juke.profileservice.feature.profile.domain.success

enum class ProfileStatusEnum {
    UPDATED;

    fun getMessage() : String {
        return "profile updated"
    }
}