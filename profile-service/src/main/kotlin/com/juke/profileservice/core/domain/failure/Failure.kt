package com.juke.profileservice.core.domain.failure

interface Failure {
    val code: Int
    val status: String
    val message: String
}