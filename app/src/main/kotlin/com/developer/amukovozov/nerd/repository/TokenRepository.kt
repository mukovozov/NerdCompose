package com.developer.amukovozov.nerd.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject

class TokenRepository @Inject constructor(
    private val preferences: SharedPreferences
) {
    companion object {
        private const val TOKEN = "token"
    }

    fun putToken(token: String) {
        preferences.edit {
            putString(TOKEN, token)
        }
    }

    fun getToken() = preferences.getString(TOKEN,
        //todo добавить авторизацию
        "")

    fun clearToken() = preferences.edit { remove(TOKEN) }
}