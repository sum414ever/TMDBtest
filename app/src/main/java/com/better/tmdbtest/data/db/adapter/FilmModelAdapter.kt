package com.better.tmdbtest.data.db.adapter

typealias DatabaseModel = com.better.tmdbtest.data.db.entity.DBFilm
typealias DomainModel = com.better.tmdbtest.domain.entity.Film

object FilmModelAdapter : DatabaseModelAdapter<DatabaseModel, DomainModel>() {

    override fun toDomainModel(networkEntity: DatabaseModel)= with(networkEntity) {
        DomainModel(
            id = externalId,
            imageUrl = imageUrl,
            title = title,
            description = description,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            isFavorite = isFavorite
        )
    }

    override fun fromDomainModel(domainEntity: DomainModel)= with(domainEntity) {
        DatabaseModel(
            externalId = id,
            imageUrl = imageUrl,
            title = title,
            description = description,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            isFavorite = isFavorite
        )
    }
}