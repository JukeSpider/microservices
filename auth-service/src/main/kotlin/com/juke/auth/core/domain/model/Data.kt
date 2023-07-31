package com.juke.auth.core.domain.model

import com.juke.auth.core.domain.failure.Failure

sealed class Data<T : Any> {

    class Success<T : Any>(val data: T) : Data<T>()
    class Error<T : Any>(val failure: Failure) : Data<T>()

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

val <T : Any> Data<T>.value: T?
    get() {
        return when (this) {
            is Data.Success -> this.data
            is Data.Error -> null
        }
    }

val <T : Any> Data<T>.failure: Failure?
    get() {
        return when (this) {
            is Data.Success -> null
            is Data.Error -> this.failure
        }
    }