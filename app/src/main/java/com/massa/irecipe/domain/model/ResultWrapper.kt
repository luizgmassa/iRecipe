package com.massa.irecipe.domain.model

sealed class ResultWrapper<out T> {
    data class Success<out T>(val value: T) : ResultWrapper<T>()
    data object NetworkError : ResultWrapper<Nothing>()
}