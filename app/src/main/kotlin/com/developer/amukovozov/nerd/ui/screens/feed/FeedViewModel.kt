package com.developer.amukovozov.nerd.ui.screens.feed

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.developer.amukovozov.nerd.model.*
import com.developer.amukovozov.nerd.util.rx.schedulersIoToMain
import com.developer.amukovozov.nerd.util.ui.*
import io.reactivex.rxjava3.core.Single
import java.util.*
import java.util.concurrent.TimeUnit

class FeedViewModel : BaseViewModel() {
    var viewState by mutableStateOf(FeedViewState())

    init {
        loadFeeds()
    }

    private fun loadFeeds() {
        Single.just(createTestData())
            .toObservable()
            .map<ScreenState<List<Feed>>> { Content(it) }
            .startWithItem(Loading())
            //todo remove delay
            .delay(5, TimeUnit.SECONDS)
            //todo add logging
            .doOnError { }
            .onErrorReturn { Stub(it) }
            .schedulersIoToMain()
            .subscribe { screenState ->
                viewState = viewState.copy(screenState = screenState)
            }
            .disposeOnViewModelDestroy()
    }

    private fun createTestData() = listOf(
        Feed(
            0, false, 2, FeedType.Review, Date().time, "233423sdfnksdf",
            Movie(438650, "", "Снегоуборщик"),
            listOf(Tag(1, "test1"), Tag(2, "test2")),
            UserInfo(1, "mail@info.com", "nickname")
        ),
        Feed(
            0, false, 2, FeedType.Review, Date().time, "233423sdfnksdf",
            Movie(438650, "", "Снегоуборщик"),
            listOf(Tag(1, "test1"), Tag(2, "test2")),
            UserInfo(1, "mail@info.com", "nickname")
        ),
        Feed(
            0, false, 2, FeedType.Review, Date().time, "233423sdfnksdf",
            Movie(438650, "", "Снегоуборщик"),
            listOf(Tag(1, "test1"), Tag(2, "test2")),
            UserInfo(1, "mail@info.com", "nickname")
        )
    )

}