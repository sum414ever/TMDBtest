package com.better.tmdbtest.domain.entity

import com.better.tmdbtest.R

data class UserToken(
    val userId: String? = null,
    val token: String? = null,
    val name: String = "Anonymous",
    val isUserLogin: Boolean = false
) {
    val imageUrl
        get() = if (userId.isNullOrBlank()) {
            R.drawable.anonymous_logo
        } else {
            "https://graph.facebook.com/$userId/picture?type=large&access_token=$token"
        }
}
