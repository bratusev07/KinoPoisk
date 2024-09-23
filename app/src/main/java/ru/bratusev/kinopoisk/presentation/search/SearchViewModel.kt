package ru.bratusev.kinopoisk.presentation.search

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.Film
import ru.bratusev.domain.usecase.GetFilmsUseCase

class SearchViewModel(private val getFilmsUseCase: GetFilmsUseCase) : ViewModel() {

    private val mutableFilmList = MutableLiveData<ArrayList<Film>>()
    internal val filmList: LiveData<ArrayList<Film>> = mutableFilmList

    private val mutableYear = MutableLiveData<String>()
    internal val year: LiveData<String> = mutableYear

    internal fun getFilmsRemote() {
        getFilmsUseCase.invoke().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("SearchFragment", "Resource.Success")
                    mutableFilmList.value = result.data as ArrayList<Film>
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

    internal fun logout() {

    }

    internal fun sortFilms() {
    }
}