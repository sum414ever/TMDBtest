package com.better.tmdbtest.domain.usecase

import com.better.tmdbtest.data.db.adapter.FilmModelAdapter
import com.better.tmdbtest.domain.FilmsRepository
import com.better.tmdbtest.domain.entity.Film
import com.better.tmdbtest.domain.entity.LoadStatus
import com.better.tmdbtest.domain.entity.UserToken
import javax.inject.Inject

class MainActivityUseCase @Inject constructor(private val filmsRepository: FilmsRepository) {

    fun saveFBToken(token: UserToken) {
        filmsRepository.saveFBToken(token)
    }

    fun loadFBToken(): UserToken {
        return filmsRepository.loadFBToken()
    }

    fun savePageIndex(page: Int) {
        filmsRepository.savePageIndex(page)
    }

    fun loadPageIndex(): Int {
        return filmsRepository.loadPageIndex()
    }

    suspend fun getFilmsList(page: Int, isCacheAccepted: Boolean): LoadStatus<List<Film>> =
        filmsRepository.getFilmsList(page, isCacheAccepted)

    suspend fun refreshList() {
        filmsRepository.refreshList()
    }

    suspend fun addToFavorite(film: Film) {
        with(FilmModelAdapter) {
            filmsRepository.addToFavorite(film.convertFromDomainModel())
        }
    }
}