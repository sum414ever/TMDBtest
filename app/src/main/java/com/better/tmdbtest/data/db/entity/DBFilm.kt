package com.better.tmdbtest.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "films",
    indices = [
        Index(value = ["external_id"], unique = true)
    ]
)
data class DBFilm(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "external_id") val externalId: Int,
    val imageUrl: String,
    val title: String,
    val description: String,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    var isFavorite: Boolean = false
)