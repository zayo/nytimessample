package cz.nedbalek.nytimessample.ui.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.nedbalek.nytimessample.connection.Api
import cz.nedbalek.nytimessample.connection.ArticlesResponse
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType
import cz.nedbalek.nytimessample.ui.fragment.BaseContentViewModel.State
import cz.nedbalek.nytimessample.ui.fragment.BaseContentViewModel.State.Data
import cz.nedbalek.nytimessample.ui.fragment.BaseContentViewModel.State.Failed
import cz.nedbalek.nytimessample.ui.fragment.BaseContentViewModel.State.Idle
import cz.nedbalek.nytimessample.ui.fragment.BaseContentViewModel.State.Loading
import cz.nedbalek.nytimessample.viewobjects.Article
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for [load] the [state] represented by [State.Data].
 */
class BaseContentViewModel : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    fun load(contentType: ContentType) {
        if (_state.value == Loading) {
            // Another loading in progress, let it finish first.
            return
        }
        _state.tryEmit(Loading)
        viewModelScope.launch {
            runCatching {
                val response = contentType.call()
                _state.emit(Data(response.results))
            }.getOrElse {
                _state.emit(Failed)
            }
        }
    }

    /**
     * States of the [BaseContentViewModel.state].
     *
     * @property Idle initial state, proceed with calling [load].
     * @property Loading state when network call is being made.
     * @property Failed network error occurred.
     * @property Data loading finished with data in [Data.articles].
     */
    sealed class State {
        object Idle : State()
        object Loading : State()
        object Failed : State()
        class Data(val articles: List<Article>) : State()
    }
}

/**
 * Converts the [ContentType] to suspend callable reference.
 */
private val ContentType.call: suspend () -> ArticlesResponse
    inline get() = when (this) {
        ContentType.MAILED -> Api::mostMailed
        ContentType.SHARED -> Api::mostShared
        ContentType.VIEWED -> Api::mostViewed
    }