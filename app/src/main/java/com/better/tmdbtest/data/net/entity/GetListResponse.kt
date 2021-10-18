package com.better.tmdbtest.data.net.entity

import com.google.gson.annotations.SerializedName

data class GetListResponse(
    @SerializedName("created_by") val createdBy: String,
    @SerializedName("description") val description: String,
    @SerializedName("favorite_count") val favoriteCount: Int,
    @SerializedName("id") val id: String,
    @SerializedName("iso_639_1") val iso6391: String,
    @SerializedName("item_count") val itemCount: Int,
    @SerializedName("items") val films: List<NetFilm>,
    @SerializedName("name") val name: String,
    @SerializedName("poster_path") val posterPath: String
)

data class NetFilm(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("genre_ids") val genreIds: List<Int>,
    @SerializedName("id") val id: Int,
    @SerializedName("media_type") val mediaType: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
)