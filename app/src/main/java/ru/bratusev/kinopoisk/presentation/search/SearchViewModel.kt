package ru.bratusev.kinopoisk.presentation.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.Film
import ru.bratusev.domain.usecase.GetFilmByKeywordUseCase
import ru.bratusev.domain.usecase.GetFilmsUseCase
import ru.bratusev.kinopoisk.common.SingleLiveEvent
import ru.bratusev.kinopoisk.presentation.mapper.SearchScreenMapper

class SearchViewModel(
    private val getFilmsUseCase: GetFilmsUseCase,
    private val getFilmByKeywordUseCase: GetFilmByKeywordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchScreenState())
    val uiState: StateFlow<SearchScreenState> = _uiState.asStateFlow()

    private val _uiLabels = SingleLiveEvent<SearchLabel>()
    val uiLabels: LiveData<SearchLabel> get() = _uiLabels

    private fun getFilmsRemote() {
        val params = uiState.value.params
        getFilmsUseCase
            .invoke(params)
            .onEach { result ->
                handleFilmResult(result, params.needUpdate)
            }.launchIn(viewModelScope)
    }

    private fun searchFilms(keyword: String) {
        getFilmByKeywordUseCase
            .invoke(keyword)
            .onEach { result ->
                handleSearchResult(result)
            }.launchIn(viewModelScope)
    }

    private fun handleFilmResult(
        result: Resource<List<Film>>,
        needUpdate: Boolean,
    ) {
        when (result) {
            is Resource.Success -> {
                updateFilmList(result.data.orEmpty(), needUpdate)
            }

            is Resource.Error -> logError(result.message)
            is Resource.Loading -> {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = true)
                }
                logLoading()
            }
        }
    }

    private fun handleSearchResult(result: Resource<List<Film>>) {
        when (result) {
            is Resource.Success -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        filmList = (SearchScreenMapper().transform(result.data.orEmpty())),
                        isLoading = false,
                        isRefreshing = false,
                    )
                }
            }

            is Resource.Error -> logError(result.message)
            is Resource.Loading -> {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = true)
                }
                logLoading()
            }
        }
    }

    private fun updateFilmList(
        newFilms: List<Film>,
        needUpdate: Boolean,
    ) {
        val newPage = SearchScreenMapper().transform(newFilms)
        val items =
            if (needUpdate) {
                newPage
            } else {
                _uiState.value.filmList.plus(newPage)
            }
        _uiState.update { currentState ->
            currentState.copy(
                filmList = items,
                isLoading = false,
                isRefreshing = false,
            )
        }
    }

    private fun logError(message: String?) {
        Log.d("SearchViewModel", "Resource.Error ${message ?: "Unknown error"}")
    }

    private fun logLoading() {
        Log.d("SearchViewModel", "Resource.Loading")
    }

    private fun sortFilms() {
        with(uiState.value) {
            params.page = 1
            params.order = if (params.order == "RATING") "YEAR" else "RATING"
            rotationSortIcon += 180
            params.needUpdate = true
        }
        getFilmsRemote()
    }

    private fun getFilmsByYear(
        selectedYear: String,
        isStart: Boolean,
    ) {
        with(uiState.value.params) {
            page = 1
            if (isStart) {
                year = selectedYear
            } else {
                endYear = selectedYear
            }
            needUpdate = true
        }
        getFilmsRemote()
    }

    private fun refreshFilms() {
        with(uiState.value.params) {
            page = 1
            needUpdate = true
        }
        uiState.value.isRefreshing = true
        getFilmsRemote()
    }

    private fun getNextPage() {
        ++uiState.value.params.page
        getFilmsRemote()
    }

    internal fun handleEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnClickBack -> _uiLabels.value = SearchLabel.GoToPrevious
            is SearchEvent.OnClickFilmItem -> _uiLabels.value = SearchLabel.GoToNext(event.bundle)
            is SearchEvent.OnClickYearPicker -> _uiLabels.value = SearchLabel.ShowDatePicker(event.isStart)
            SearchEvent.OnRefresh -> refreshFilms()
            is SearchEvent.OnScrollDown -> handelOnScrollDown(event.inputSearch)
            SearchEvent.OnSort -> sortFilms()
            SearchEvent.OnFragmentStart -> getFilmsRemote()
            is SearchEvent.OnSearchFilms -> searchFilms(event.inputSearch)
            is SearchEvent.OnYearSelected -> getFilmsByYear(event.selectedYear, event.isStart)
        }
    }

    private fun handelOnScrollDown(inputSearch: String) {
        if (inputSearch.isEmpty()) {
            getNextPage()
        } else {
            _uiLabels.value = SearchLabel.ShowToast("Это все ответы найденые локально и удовлетворяющие вашему запросу")
        }
    }
}
