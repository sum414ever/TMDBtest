package com.better.tmdbtest.data.db

import androidx.room.*
import com.better.tmdbtest.data.db.entity.DBFilm

@Dao
interface FilmDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFilm(film: DBFilm)

    @Query("SELECT * FROM films")
    suspend fun getAllFilms(): List<DBFilm>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertListOfFilms(films: List<DBFilm>)

    @Query("UPDATE films SET isFavorite = :favorite WHERE external_id = :id")
    suspend fun updateFavoriteFilm(id: Int, favorite: Boolean)

    @Query("DELETE FROM films")
    suspend fun deleteAll()

}