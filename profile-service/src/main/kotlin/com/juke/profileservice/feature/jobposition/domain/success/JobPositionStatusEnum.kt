package com.juke.profileservice.feature.jobposition.domain.success

enum class JobPositionStatusEnum {
    CREATED,
    UPDATED,
    DELETED;

    fun getMessage(): String {
        if (this == CREATED) return "job position creation completed"
        if (this == UPDATED) return "job position update completed"
        return "job position deletion completed"
    }
}