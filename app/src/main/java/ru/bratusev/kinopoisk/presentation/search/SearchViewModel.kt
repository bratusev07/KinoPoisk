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

    private val mutableFilmList = MutableLiveData<ArrayList<Film>>()
    internal val filmList: LiveData<ArrayList<Film>> = mutableFilmList

    private val mutableIsLoading = MutableLiveData<Boolean>()
    internal val isLoading: LiveData<Boolean> = mutableIsLoading

    private val mutableYear = MutableLiveData<String>()
    internal val year: LiveData<String> = mutableYear

    internal fun getFilmsRemote(order: String = "RATING", year: String = "1000", page: Int = 1, needUpdate: Boolean = false) {
        getFilmsUseCase.invoke(order, year, page).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("SearchFragment", "Resource.Success")
                    if(needUpdate){
                        mutableFilmList.value = result.data as ArrayList<Film>
                    }else{
                        val films = mutableFilmList.value ?: arrayListOf()
                        films.addAll(result.data as ArrayList<Film>)
                        mutableFilmList.value = films
                    }
                    mutableIsLoading.value = false
                }

                is Resource.Error -> {
                    Log.d("SearchFragment", "Resource.Error ${result.message.toString()}")
                }

                is Resource.Loading -> {
                    mutableIsLoading.value = true
                    Log.d("SearchFragment", "Resource.Loading")
                }
            }
        }.launchIn(viewModelScope)
    }

    internal fun searchFilms(keyword: String) {
        getFilmByKeywordUseCase.invoke(keyword).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("SearchFragment", "Resource.Success")
                    mutableFilmList.value = result.data as ArrayList<Film>
                    mutableIsLoading.value = false
                }

                is Resource.Error -> {
                    Log.d("SearchFragment", "Resource.Error ${result.message.toString()}")
                }

                is Resource.Loading -> {
                    mutableIsLoading.value = true
                    Log.d("SearchFragment", "Resource.Loading")
                }
            }
        }.launchIn(viewModelScope)
    }
}