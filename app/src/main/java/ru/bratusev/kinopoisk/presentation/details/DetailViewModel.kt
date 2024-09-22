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

    private val mutableFilm = MutableLiveData<FilmDetail>()
    internal val filmDetail: LiveData<FilmDetail> = mutableFilm

    private val mutableFrameList = MutableLiveData<ArrayList<Frame>>()
    internal val frameList: LiveData<ArrayList<Frame>> = mutableFrameList

    internal fun getFilmByIdRemote(kinopoiskId: Int) {
        getFilmByIdUseCase.invoke(kinopoiskId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("SearchFragment", "Resource.Success")
                    mutableFilm.value = result.data as FilmDetail
                }

                is Resource.Error -> {
                    Log.d("SearchFragment", "Resource.Error ${result.message.toString()}")
                }

                is Resource.Loading -> {
                    Log.d("SearchFragment", "Resource.Loading")
                }
            }
        }.launchIn(viewModelScope)
    }

    internal fun getFramesRemote(kinopoiskId: Int) {
        getFramesUseCase.invoke(kinopoiskId).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("SearchFragment", "Resource.Success")
                    mutableFrameList.value = result.data as ArrayList<Frame>
                }

                is Resource.Error -> {
                    Log.d("SearchFragment", "Resource.Error ${result.message.toString()}")
                }

                is Resource.Loading -> {
                    Log.d("SearchFragment", "Resource.Loading")
                }
            }
        }.launchIn(viewModelScope)
    }

}