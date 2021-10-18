package com.better.tmdbtest.data

import android.util.Log
import com.better.tmdbtest.data.db.FilmDao
import com.better.tmdbtest.data.db.entity.DBFilm
import com.better.tmdbtest.data.net.FilmApi
import com.better.tmdbtest.data.prefs.PreferencesAPI
import com.better.tmdbtest.domain.FilmsRepository
import com.better.tmdbtest.domain.entity.Film
import com.better.tmdbtest.domain.entity.LoadStatus
import com.better.tmdbtest.domain.entity.UserToken
import javax.inject.Inject

class FilmsRepositoryImpl @Inject constructor(
    private val prefs: PreferencesAPI,
    private val filmDao: FilmDao,
    private val filmApi: FilmApi
) : FilmsRepository {

    override fun saveFBToken(token: UserToken) {
        prefs.saveFBToken(token)
    }

    override fun loadFBToken(): UserToken {
        return prefs.loadFBToken()
    }

    override fun savePageIndex(page: Int) {
        prefs.savePageIndex(page)
    }

    override fun loadPageIndex(): Int {
        return prefs.loadPageIndex()
    }

    override suspend fun getFilmsList(page: Int, isCacheAccepted: Boolean): LoadStatus<List<Film>> =
        try {
            if (isCacheAccepted) {
                val cachedFilms = with(com.better.tmdbtest.data.db.adapter.FilmModelAdapter) {
                    filmDao.getAllFilms().convertToDomainModel()
                }
                LoadStatus.Success(cachedFilms)
            }

            //Get films from API
            val response = filmApi.getMovieList(page).films
            val domainFilms = with(com.better.tmdbtest.data.net.adapter.FilmModelAdapter) {
                response.convertToDBModel()
            }
            //Store to DB
            with(com.better.tmdbtest.data.db.adapter.FilmModelAdapter) {
                filmDao.insertListOfFilms(domainFilms.convertFromDomainModel())
            }

            LoadStatus.Success(domainFilms)
        } catch (e: Exception) {
            if (page > 1) LoadStatus.Error(e)
            else {
                val result = with(com.better.tmdbtest.data.db.adapter.FilmModelAdapter) {
                    filmDao.getAllFilms().convertToDomainModel()
                }
                if (result.isNotEmpty()) LoadStatus.Success(result)
                else LoadStatus.Error(e)
            }
        }

    override suspend fun refreshList() =
        filmDao.deleteAll()

    override suspend fun addToFavorite(film: DBFilm) =
        filmDao.updateFavoriteFilm(film.externalId, film.isFavorite)

}