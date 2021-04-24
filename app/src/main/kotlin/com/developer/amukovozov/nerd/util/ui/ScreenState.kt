package com.developer.amukovozov.nerd.util.ui

sealed class ScreenState<T>

data class Content<T>(val content: T? = null) : ScreenState<T>()
class Loading<T> : ScreenState<T>()
data class Stub<T>(val error: Throwable? = null) : ScreenState<T>()