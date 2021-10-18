package com.better.tmdbtest.data.db

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.better.tmdbtest.data.db.entity.DBFilm
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class FilmDatabaseTest() {

    @Test
    fun filmDao() = runBlocking {
        val film = DBFilm(
            externalId = 284053,
            imageUrl = "https://image.tmdb.org/t/p/w500/rzRwTcFvttcN1ZpX2xv4j3tSdJu.jpg",
            title = "Thor: Ragnarok",
            description = "Thor is imprisoned on the other side of the universe and finds himself in a race against time to get back to Asgard to stop Ragnarok, the destruction of his home-world and the end of Asgardian civilization, at the hands of a powerful new threat, the ruthless Hela.",
            releaseDate = "2017-10-25",
            voteAverage = 7.6,
            voteCount = 16568,
            isFavorite = false
        )

    }
}

