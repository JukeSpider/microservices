package com.juke.auth.features.registration.data.entity.enums

enum class FlowTypeEnum {
    REGISTRATION,
    PASSWORD_RESET,
    UNDEFINED;

    companion object {
        fun convertToFlow(flow: String): FlowTypeEnum {
            return when (flow.lowercase()) {
                "registration" -> REGISTRATION
                "password_reset" -> PASSWORD_RESET
                else -> UNDEFINED
            }
        }
    }
}