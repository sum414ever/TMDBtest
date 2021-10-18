package com.better.tmdbtest.data.prefs

import android.content.Context
import com.better.tmdbtest.domain.entity.UserToken
import com.google.gson.Gson
import javax.inject.Inject

class PreferencesAPI @Inject constructor(private val context: Context) {

    companion object {
        private const val PREFS = "MY_PREFS"
        private const val TOKEN = "TOKEN"
        private const val PAGE = "PAGE"
    }

    fun saveFBToken(token: UserToken) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val gsonToken = Gson().toJson(token)
        prefs.edit().putString(TOKEN, gsonToken).apply()
    }

    fun loadFBToken(): UserToken {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val gsonToken = prefs.getString(TOKEN, "") ?: ""
        return if (gsonToken.isEmpty()) UserToken()
        else Gson().fromJson(gsonToken, UserToken::class.java)
    }

    fun savePageIndex(page: Int) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putInt(PAGE, page).apply()
    }

    fun loadPageIndex(): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getInt(PAGE, 1)
    }

}