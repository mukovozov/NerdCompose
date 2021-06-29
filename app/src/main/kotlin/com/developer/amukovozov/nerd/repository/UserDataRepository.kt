package com.developer.amukovozov.nerd.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataRepository @Inject constructor(
    private val preferences: SharedPreferences
) {
    companion object {
        private const val TOKEN = "token"
        private const val USER_ID = "user_id"
    }

    fun saveMyUserId(userId: Int) {
        preferences.edit {
            putInt(USER_ID, userId)
        }
    }

    fun getMyUserId() = preferences.getInt(USER_ID, 0)

    fun putToken(token: String) {
        preferences.edit {
            putString(TOKEN, token)
        }
    }

    fun getToken() = preferences.getString(TOKEN, "")

    fun clearToken() = preferences.edit { remove(TOKEN) }
}