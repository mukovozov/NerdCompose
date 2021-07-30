package com.developer.amukovozov.nerd.network.pagination

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.util.concurrent.Executors
import javax.inject.Inject

object Paginator {

    const val START_PAGE = 0

    sealed class State {
        object Empty : State()
        object EmptyProgress : State()
        data class EmptyError(val error: Throwable) : State()
        data class Data<T>(val pageCount: Int, val data: List<T>) : State()
        data class Refresh<T>(val pageCount: Int, val data: List<T>) : State()
        data class NewPageProgress<T>(val pageCount: Int, val data: List<T>) : State()
        data class FullData<T>(val pageCount: Int, val data: List<T>) : State()
    }

    sealed class Action {
        object Refresh : Action()
        object Restart : Action()
        object LoadMore : Action()
        data class NewPage<T>(val pageNumber: Int, val items: List<T>) : Action()
        data class PageError(val error: Throwable) : Action()
    }

    sealed class SideEffect {
        data class LoadPage(val currentPage: Int) : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
    }

    private fun <T> reducer(
        action: Action,
        state: State,
        sideEffectListener: (SideEffect) -> Unit
    ): State =
        when (action) {
            is Action.Refresh -> {
                sideEffectListener(SideEffect.LoadPage(START_PAGE))
                when (state) {
                    is State.Empty -> State.EmptyProgress
                    is State.EmptyError -> State.EmptyProgress
                    is State.Data<*> -> State.Refresh(state.pageCount, state.data as List<*>)
                    is State.NewPageProgress<*> -> State.Refresh(
                        state.pageCount,
                        state.data as List<*>
                    )
                    is State.FullData<*> -> State.Refresh(state.pageCount, state.data as List<*>)
                    else -> state
                }
            }
            is Action.Restart -> {
                sideEffectListener(SideEffect.LoadPage(START_PAGE))
                when (state) {
                    is State.Empty -> State.EmptyProgress
                    is State.EmptyError -> State.EmptyProgress
                    is State.Data<*> -> State.EmptyProgress
                    is State.Refresh<*> -> State.EmptyProgress
                    is State.NewPageProgress<*> -> State.EmptyProgress
                    is State.FullData<*> -> State.EmptyProgress
                    else -> state
                }
            }
            is Action.LoadMore -> {
                when (state) {
                    is State.Data<*> -> {
                        sideEffectListener(SideEffect.LoadPage(state.pageCount + 1))
                        State.NewPageProgress(state.pageCount, state.data as List<*>)
                    }
                    else -> state
                }
            }
            is Action.NewPage<*> -> {
                val items = action.items as List<*>
                when (state) {
                    is State.EmptyProgress -> {
                        if (items.isEmpty()) {
                            State.Empty
                        } else {
                            State.Data(START_PAGE, items)
                        }
                    }
                    is State.Refresh<*> -> {
                        if (items.isEmpty()) {
                            State.Empty
                        } else {
                            State.Data(START_PAGE, items)
                        }
                    }
                    is State.NewPageProgress<*> -> {
                        if (items.isEmpty()) {
                            State.FullData(state.pageCount, state.data as List<*>)
                        } else {
                            State.Data(state.pageCount + 1, state.data as List<*> + items)
                        }
                    }
                    else -> state
                }
            }
            is Action.PageError -> {
                when (state) {
                    is State.EmptyProgress -> State.EmptyError(action.error)
                    is State.Refresh<*> -> {
                        sideEffectListener(Paginator.SideEffect.ErrorEvent(action.error))
                        State.Data(state.pageCount, state.data as List<*>)
                    }
                    is State.NewPageProgress<*> -> {
                        sideEffectListener(Paginator.SideEffect.ErrorEvent(action.error))
                        State.Data(state.pageCount, state.data as List<*>)
                    }
                    else -> state
                }
            }
        }

    class Store<T> @Inject constructor() {
        private var state: State = State.Empty
        var render: (State) -> Unit = {}
            set(value) {
                field = value
                value(state)
            }

        private val sideEffectsExecutor = Executors.newSingleThreadExecutor()
        private val sideEffectRelay = PublishSubject.create<SideEffect>()
        val sideEffects: Observable<SideEffect> =
            sideEffectRelay
                .hide()
                .observeOn(AndroidSchedulers.mainThread())


        fun proceed(action: Action) {
            Timber.d("Action: $action")
            val newState = reducer<T>(action, state) { sideEffect ->
                sideEffectsExecutor.submit { sideEffectRelay.onNext(sideEffect) }
            }
            if (newState != state) {
                state = newState
                Timber.d("New state: $state")
                render(state)
            }
        }
    }
}
