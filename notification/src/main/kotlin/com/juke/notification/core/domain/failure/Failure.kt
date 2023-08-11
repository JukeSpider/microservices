package com.juke.notification.core.domain.failure

interface Failure {
    val code: Int
    val status: String
    val message: String
}