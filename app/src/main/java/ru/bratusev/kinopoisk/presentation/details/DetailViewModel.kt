package ru.bratusev.kinopoisk.presentation.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.FilmDetail
import ru.bratusev.domain.model.Frame
import ru.bratusev.domain.usecase.GetFilmByIdUseCase
import ru.bratusev.domain.usecase.GetFramesUseCase

class DetailViewModel(
    private val getFilmByIdUseCase: GetFilmByIdUseCase,
    private val getFramesUseCase: GetFramesUseCase
) : ViewModel() {

    private val _filmDetail = MutableLiveData<FilmDetail>()
    val filmDetail: LiveData<FilmDetail> = _filmDetail

    private val _frameList = MutableLiveData<List<Frame>>()
    val frameList: LiveData<List<Frame>> = _frameList

    internal fun getFilmByIdRemote(kinopoiskId: Int) {
        getFilmByIdUseCase.invoke(kinopoiskId).onEach { result ->
            handleFilmResult(result)
        }.launchIn(viewModelScope)
    }

    private fun handleFilmResult(result: Resource<FilmDetail>) {
        when (result) {
            is Resource.Success -> _filmDetail.value = result.data as FilmDetail
            is Resource.Error -> logError(result.message)
            is Resource.Loading -> logLoading()
        }
    }

    internal fun getFramesRemote(kinopoiskId: Int) {
        getFramesUseCase.invoke(kinopoiskId).onEach { result ->
            handleFramesResult(result)
        }.launchIn(viewModelScope)
    }

    private fun handleFramesResult(result: Resource<ArrayList<Frame>>) {
        when (result) {
            is Resource.Success -> _frameList.value = result.data ?: emptyList()
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
