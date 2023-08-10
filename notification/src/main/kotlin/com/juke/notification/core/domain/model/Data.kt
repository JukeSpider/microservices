package com.juke.notification.core.domain.model

import com.juke.notification.core.domain.failure.Failure


sealed class Data<T> {

    class Success<T>(val data: T) : Data<T>()
    class Error<T>(val failure: Failure) : Data<T>()

    override fun equals(other: Any?): Boolean {
        return when {
            (this is Success && other is Success<*>) -> (this.data == other.data)
            (this is Error && other is Error<*>) -> (this.failure == other.failure)
            else -> super.equals(other)
        }
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

val <T> Data<T>.value: T?
    get() {
        return when (this) {
            is Data.Success -> this.data
            is Data.Error -> null
        }
    }

val <T> Data<T>.failure: Failure?
    get() {
        return when (this) {
            is Data.Success -> null
            is Data.Error -> this.failure
        }
    }