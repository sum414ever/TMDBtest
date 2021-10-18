package com.better.tmdbtest.domain.entity

data class Film(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val description: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    var isFavorite:Boolean = false
) : Item