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
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImFuZHJleUBtdWtvdm96b3YucnVzIiwiZXhwIjoxNjI3OTE3MjA3LCJvcmlnX2lhdCI6MTYxOTM2MzYwNywicGFzc3dvcmQiOiJwYXNzd29yZDEyMyJ9.qBcmv_28vGCkIWbQycTUtQFG0AjIb1cCmge9Phy8WPA")

    fun clearToken() = preferences.edit { remove(TOKEN) }
}