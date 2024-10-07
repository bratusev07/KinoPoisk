package ru.bratusev.kinopoisk.presentation.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.domain.model.Frame
import ru.bratusev.domain.usecase.GetFilmByIdUseCase
import ru.bratusev.domain.usecase.GetFramesUseCase
import ru.bratusev.kinopoisk.presentation.mapper.DetailScreenMapper

class DetailViewModel(
    private val getFilmByIdUseCase: GetFilmByIdUseCase,
    private val getFramesUseCase: GetFramesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailScreenState())
    val uiState: StateFlow<DetailScreenState> = _uiState.asStateFlow()

    internal fun getFilmByIdRemote(kinopoiskId: Int) {
        getFilmByIdUseCase.invoke(kinopoiskId).onEach { result ->
            handleFilmResult(result)
        }.launchIn(viewModelScope)
    }

    private fun handleFilmResult(result: Resource<FilmDetail>) {
        when (result) {
            is Resource.Success ->_uiState.update { currentState ->
                currentState.copy(
                    filmDetail = result.data as FilmDetail
                )
            }
            is Resource.Error -> logError(result.message)
            is Resource.Loading -> logLoading()
        }
    }

    internal fun getFramesRemote(kinopoiskId: Int) {
        getFramesUseCase.invoke(kinopoiskId).onEach { result ->
            handleFramesResult(result)
        }.launchIn(viewModelScope)
    }

    private fun handleFramesResult(result: Resource<List<Frame>>) {
        when (result) {
            is Resource.Success -> _uiState.update { currentState ->
                currentState.copy(frameList = DetailScreenMapper().transform(result.data.orEmpty()))
            }
            is Resource.Error -> logError(result.message)
            is Resource.Loading -> logLoading()
        }
    }

    private fun logError(message: String?) {
        Log.d("DetailViewModel", "Resource.Error ${message ?: "Unknown error"}")
    }

    private fun logLoading() {
        Log.d("DetailViewModel", "Resource.Loading")
    }
}
