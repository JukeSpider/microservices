package com.juke.auth.features.admin.domain.success

enum class StatusEnum {
    CREATED,
    DELETED;

    fun getMessage() : String {
        if (this == CREATED) return "user is created"
        return "user is deleted"
    }
}