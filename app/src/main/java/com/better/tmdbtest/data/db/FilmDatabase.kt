package com.better.tmdbtest.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.better.tmdbtest.data.db.entity.DBFilm

@Database(
    entities = [DBFilm::class],
    version = 1
)
abstract class FilmDatabase : RoomDatabase() {
    abstract fun filmDao(): FilmDao
}