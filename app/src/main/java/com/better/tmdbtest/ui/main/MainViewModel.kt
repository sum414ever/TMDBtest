package com.better.tmdbtest.ui.main

import android.util.Log
import androidx.lifecycle.*
import com.better.tmdbtest.domain.entity.*
import com.better.tmdbtest.domain.usecase.MainActivityUseCase
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val useCase: MainActivityUseCase) : ViewModel() {

    private var _tokenLiveData = MutableLiveData<UserToken>()
    val tokenLiveData: LiveData<UserToken> = _tokenLiveData

    private var _filmsLiveData = MutableLiveData<List<Item>>()
    val filmsLiveData: LiveData<List<Item>> = _filmsLiveData

    private var _favoriteFilmsLiveData = MutableLiveData<List<Film>>()
    val favoriteFilmsLiveData: LiveData<List<Film>> = _favoriteFilmsLiveData

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState> = _viewState
    var pageIndex = 1

    private val _newFilmsPageReloadTrigger = MutableLiveData(0L)
    private val _filmsUpdate: LiveData<LoadStatus<List<Film>>> =
        Transformations.switchMap(_newFilmsPageReloadTrigger) {
            liveData(Dispatchers.IO) {
                emit(LoadStatus.Loading)
                emit(useCase.getFilmsList(pageIndex, _newFilmsPageReloadTrigger.value!! < 0))
            }
        }

    init {
        pageIndex = useCase.loadPageIndex()
        _tokenLiveData.postValue(useCase.loadFBToken())
        _filmsUpdate.observeForever { loadStatus ->
            when (loadStatus) {
                is LoadStatus.Loading ->{
                    val loading = Networking("Loading")
                    _viewState.value = ViewState(isNetworking = true)
                    val currentList = _filmsLiveData.value?.toMutableList()
                    if (currentList.isNullOrEmpty()) {
                        _filmsLiveData.value = listOf(loading)
                    } else{
                        currentList.add(0, loading)
                        currentList.add(currentList.size, loading)
                        _filmsLiveData.value = currentList!!
                    }
                }
                is LoadStatus.Error -> {
                    val currentList = _filmsLiveData.value?.filterIsInstance<Film>()?.toMutableList<Item>()
                    val error = Networking(isItError = true, message = loadStatus.e.message?: "Error")
                    if (currentList.isNullOrEmpty()) {
                        _filmsLiveData.value = listOf(error)
                    } else {
                        currentList.add(0, error)
                        currentList.add(currentList.size, error)
                        _filmsLiveData.value = currentList!!
                    }
                    _viewState.value =
                        ViewState(errorMessage = loadStatus.e.message)
                }
                is LoadStatus.Success -> {
                    _viewState.value = ViewState(isNetworking = false)
                    _filmsLiveData.value = _filmsLiveData.value?.filterIsInstance<Film>()?.plus(loadStatus.data)
                    useCase.savePageIndex(pageIndex)
                }
            }
        }
        _favoriteFilmsLiveData.value = _filmsLiveData.value?.filterIsInstance<Film>()?.filter { it.isFavorite } ?: emptyList()
        loadFilmsPageAcceptsCache()
    }

    fun saveAnonymousUser() {
        val user = UserToken(isUserLogin = true)
        _tokenLiveData.postValue(user)
        saveFBToken(user)
    }

    fun saveLoginUser() {
        val accessToken = AccessToken.getCurrentAccessToken()

        if (accessToken != null && !accessToken.isExpired) {
            GraphRequest(
                accessToken,
                accessToken.userId,
                null,
                HttpMethod.GET
            ) { response ->
                val user = UserToken(
                    userId = accessToken.userId,
                    token = accessToken.token,
                    name = response.jsonObject["name"].toString(),
                    isUserLogin = true
                )
                _tokenLiveData.postValue(user)
                saveFBToken(user)
            }.executeAsync()
        }
    }

    private fun saveFBToken(token: UserToken) {
        useCase.saveFBToken(token)
    }

    fun saveNoLoginUser() {
        saveFBToken(UserToken(isUserLogin = false))
    }

    data class ViewState(
        val isNetworking: Boolean = false,
        val errorMessage: String? = null
    )

    private fun loadFilmsPageAcceptsCache() {
        _newFilmsPageReloadTrigger.value = -1L
    }

    fun loadFilmsPage() {
        _newFilmsPageReloadTrigger.value = System.currentTimeMillis()
    }

    fun refreshList() {
        pageIndex = 1
        viewModelScope.launch {
            useCase.refreshList()
        }
        _filmsLiveData.value = emptyList()
        _favoriteFilmsLiveData.value = emptyList()
        loadFilmsPage()
    }

    fun addToFavorite(film: Film) {
        viewModelScope.launch { useCase.addToFavorite(film) }
        _filmsLiveData.value?.filterIsInstance<Film>()?.find { it.id == film.id }?.isFavorite = film.isFavorite
        _filmsLiveData.value = _filmsLiveData.value

        _favoriteFilmsLiveData.value = _filmsLiveData.value?.filterIsInstance<Film>()?.filter { it.isFavorite }
    }

    fun pressTryAgain() {
        Log.d("kek", "pressTryAgain")
        _viewState.value = ViewState(isNetworking = false)
        pageIndex++
        loadFilmsPage()
    }

}