package com.developer.amukovozov.nerd.repository

import com.developer.amukovozov.nerd.model.feed.Tag
import com.developer.amukovozov.nerd.network.NerdApi
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class TagRepository @Inject constructor(
    private val api: NerdApi
) {
    fun loadTags(): Single<List<Tag>> {
        return api.getTags()
    }
}