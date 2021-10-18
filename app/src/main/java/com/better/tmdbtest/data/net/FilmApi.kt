package com.better.tmdbtest.data.net

import com.better.tmdbtest.data.net.entity.GetListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * TMDB API communication setup via Retrofit.
 */
interface FilmApi {

    @GET("/3/list/{page}")
    suspend fun getMovieList(
        @Path("page") pageIndex: Int?
    ): GetListResponse
}