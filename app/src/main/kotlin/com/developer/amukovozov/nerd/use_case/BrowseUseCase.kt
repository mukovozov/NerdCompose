package com.developer.amukovozov.nerd.use_case

import com.developer.amukovozov.nerd.model.BrowseInformation
import com.developer.amukovozov.nerd.repository.MovieRepository
import com.developer.amukovozov.nerd.repository.TagRepository
import com.developer.amukovozov.nerd.repository.UserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@ViewModelScoped
class BrowseUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
    private val tagRepository: TagRepository,
    private val userRepository: UserRepository
) {
    fun loadBrowseScreenInfo(): Single<BrowseInformation> {
        return Single.zip(
            movieRepository.getPopularMovies(),
            tagRepository.loadTags(),
            userRepository.getUsersPopular()
        ) { movies, tags, users ->
           BrowseInformation(movies, tags, users)
        }
    }
}