package com.better.tmdbtest.data.net.adapter

import com.better.tmdbtest.BuildConfig


typealias NetworkModel = com.better.tmdbtest.data.net.entity.NetFilm
typealias DomainModel = com.better.tmdbtest.domain.entity.Film


object FilmModelAdapter : NetworkModelAdapter<NetworkModel, DomainModel>() {


    override fun toDBModel(networkEntity: NetworkModel) = with(networkEntity) {
        DomainModel(
            id = id,
            imageUrl = BuildConfig.BASE_IMAGE_URL + networkEntity.posterPath,
            title = title,
            description = overview,
            releaseDate = releaseDate ?: "",
            voteAverage = voteAverage,
            voteCount = voteCount
        )
    }
}