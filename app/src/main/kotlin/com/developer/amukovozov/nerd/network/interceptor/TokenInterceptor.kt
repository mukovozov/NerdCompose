package com.developer.amukovozov.nerd.network.interceptor

import com.developer.amukovozov.nerd.repository.UserDataRepository
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor(
    private val userDataRepository: UserDataRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = userDataRepository.getToken()
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}