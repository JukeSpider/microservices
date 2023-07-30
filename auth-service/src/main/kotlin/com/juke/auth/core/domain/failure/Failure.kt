package com.juke.auth.core.domain.failure

interface Failure {
    val code: Int
    val status: String
    val message: String
}