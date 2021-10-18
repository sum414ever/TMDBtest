package com.better.tmdbtest.domain

import com.better.tmdbtest.data.db.entity.DBFilm
import com.better.tmdbtest.domain.entity.UserToken
import com.better.tmdbtest.domain.entity.Film
import com.better.tmdbtest.domain.entity.LoadStatus

interface FilmsRepository {

    fun saveFBToken(token: UserToken)

    fun loadFBToken(): UserToken

    fun savePageIndex(page: Int)

    fun loadPageIndex(): Int

    suspend fun getFilmsList(page: Int, isCacheAccepted: Boolean): LoadStatus<List<Film>>

    suspend fun refreshList()

    suspend fun addToFavorite(film: DBFilm)
}