package ru.bratusev.kinopoisk.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.Film
import ru.bratusev.domain.usecase.GetFilmByKeywordUseCase
import ru.bratusev.domain.usecase.GetFilmsUseCase

class SearchViewModel(
    private val getFilmsUseCase: GetFilmsUseCase,
    private val getFilmByKeywordUseCase: GetFilmByKeywordUseCase
) : ViewModel() {

    private val _filmList = MutableLiveData<ArrayList<Film>>()
    internal val filmList: LiveData<ArrayList<Film>> = _filmList

    private val _isLoading = MutableLiveData<Boolean>()
    internal val isLoading: LiveData<Boolean> = _isLoading

    private val _year = MutableLiveData<String>()
    internal val year: LiveData<String> = _year

    internal fun getFilmsRemote(order: String = "RATING", year: String = "1000", page: Int = 1, needUpdate: Boolean = false) {
        getFilmsUseCase.invoke(order, year, page).onEach { result ->
            handleFilmResult(result, needUpdate)
        }.launchIn(viewModelScope)
    }

    internal fun searchFilms(keyword: String) {
        getFilmByKeywordUseCase.invoke(keyword).onEach { result ->
            handleSearchResult(result)
        }.launchIn(viewModelScope)
    }

    private fun handleFilmResult(result: Resource<ArrayList<Film>>, needUpdate: Boolean) {
        when (result) {
            is Resource.Success -> {
                updateFilmList(result.data as ArrayList<Film>, needUpdate)
                _isLoading.value = false
            }
            is Resource.Error -> logError(result.message)
            is Resource.Loading -> {
                _isLoading.value = true
                logLoading()
            }
        }
    }

    private fun handleSearchResult(result: Resource<ArrayList<Film>>) {
        when (result) {
            is Resource.Success -> {
                _filmList.value = result.data as ArrayList<Film>
                _isLoading.value = false
            }
            is Resource.Error -> logError(result.message)
            is Resource.Loading -> {
                _isLoading.value = true
                logLoading()
            }
        }
    }

    private fun updateFilmList(newFilms: ArrayList<Film>, needUpdate: Boolean) {
        if (needUpdate) {
            _filmList.value = newFilms
        } else {
            val currentFilms = _filmList.value ?: arrayListOf()
            currentFilms.addAll(newFilms)
            _filmList.value = currentFilms
        }
    }

    private fun logError(message: String?) {
        Log.d("SearchViewModel", "Resource.Error ${message ?: "Unknown error"}")
    }

    private fun logLoading() {
        Log.d("SearchViewModel", "Resource.Loading")
    }
}