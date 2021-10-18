package com.better.tmdbtest.domain.entity

/**
 * LoadStatus from a search, which contains List<Film> holding query data,
 * and a String of network error state or Loading object
 */
open class LoadStatus<out T : Any> {
    object Loading : LoadStatus<Nothing>()
    data class Success<out T : Any>(val data: T) : LoadStatus<T>()
    data class Error(val e: Exception) : LoadStatus<Nothing>()
}