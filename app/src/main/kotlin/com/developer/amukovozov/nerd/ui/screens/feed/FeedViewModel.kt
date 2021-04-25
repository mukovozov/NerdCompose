package com.developer.amukovozov.nerd.ui.screens.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.*
import com.developer.amukovozov.nerd.repository.FeedRepository
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Single
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository
) : BaseViewModel() {
    var viewState by mutableStateOf(FeedViewState())

    init {
        loadFeeds()
    }

    private fun loadFeeds() {
        feedRepository.loadFeed()
            .toObservable()
            .map<ScreenState<List<Feed>>> { Content(it) }
            .startWithItem(Loading())
            //todo add logging
            .doOnError(Timber::e)
            .onErrorReturn { Stub(it) }
            .schedulersIoToMain()
            .subscribe { screenState ->
                viewState = viewState.copy(screenState = screenState)
            }
            .disposeOnViewModelDestroy()
    }
}