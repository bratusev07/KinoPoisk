package ru.bratusev.kinopoisk.presentation.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.common.DistinctTextWatcher
import ru.bratusev.kinopoisk.common.NetworkUtils
import ru.bratusev.kinopoisk.databinding.FragmentSearchBinding
import ru.bratusev.kinopoisk.presentation.items.FilmArgs

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val vm by viewModels<SearchViewModel>()

    private val viewBinding: FragmentSearchBinding by viewBinding()

    private val filmAdapter =
        FilmAdapter { film ->
            onItemClick(film)
        }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        setObservers()
        loadInitialData()
    }

    private fun loadInitialData() {
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            vm.handleEvent(SearchEvent.OnFragmentStart)
        }
    }

    private fun configureViews() {
        setupLogoutButton()
        setupSearchInput()
        setupSortButton()
        setupYearPicker()
        setupSwipeRefresh()
        setupRecyclerView()
        setupBackPressHandler()
    }

    private fun setupLogoutButton() {
        viewBinding.imageLogout.setOnClickListener {
            vm.handleEvent(SearchEvent.OnClickBack)
        }
    }

    private fun setupSearchInput() {
        viewBinding.inputSearch.doOnTextUpdated { vm.handleEvent(SearchEvent.OnSearchFilms(it)) }
    }

    private fun setupSortButton() {
        viewBinding.imageSort.setOnClickListener {
            vm.handleEvent(SearchEvent.OnSort)
        }
    }

    private fun setupYearPicker() {
        viewBinding.startYearPicker.setOnClickListener {
            vm.handleEvent(SearchEvent.OnClickYearPicker(true))
        }
        viewBinding.endYearPicker.setOnClickListener {
            vm.handleEvent(SearchEvent.OnClickYearPicker(false))
        }
    }

    private fun showYearPickerDialog(isStart: Boolean = true) {
        val dataPicker =
            DatePickerDialog(requireContext(), { _, selectedYear, _, _ ->
                vm.handleEvent(SearchEvent.OnYearSelected(selectedYear.toString(), isStart))
            }, 2024, 1, 1).apply {
                datePicker.maxDate = System.currentTimeMillis()
            }
        dataPicker.show()
    }

    private fun setupSwipeRefresh() {
        viewBinding.swipeRefreshFilms.setOnRefreshListener {
            vm.handleEvent(SearchEvent.OnRefresh)
        }
    }

    private fun setupRecyclerView() {
        viewBinding.recyclerFilms.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = filmAdapter
            addOnScrollListener(onScroll)
            addItemDecoration(filmItemUIDecorator)
        }
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    vm.handleEvent(SearchEvent.OnClickBack)
                }
            },
        )
    }

    private val onScroll =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
            ) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                    vm.handleEvent(SearchEvent.OnScrollDown(viewBinding.inputSearch.text.toString()))
                }
            }
        }

    private fun setObservers() {
        vm.uiLabels.observe(viewLifecycleOwner) {
            when (it) {
                SearchLabel.GoToPrevious -> navigateToLoginFragment()
                is SearchLabel.GoToNext -> navigateToDetailFragment(it.film)
                is SearchLabel.ShowDatePicker -> showYearPickerDialog(it.isStart)
                is SearchLabel.ShowToast -> showToast(it.message)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            vm.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    filmAdapter.items = it.filmList
                    with(viewBinding) {
                        swipeRefreshFilms.isRefreshing = it.isRefreshing
                        progressLoad.visibility =
                            if (it.isLoading) ProgressBar.VISIBLE else ProgressBar.INVISIBLE
                        imageSort.rotationY = it.rotationSortIcon
                        startYearPicker.text = it.params.year
                        endYearPicker.text = it.params.endYear
                    }
                }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLoginFragment() {
        try {
            findNavController().navigate(R.id.action_searchFragment_to_loginFragment)
        } catch (_: RuntimeException) {
        }
    }

    private fun navigateToDetailFragment(film: FilmArgs) {
        try {
            findNavController().navigate(SearchFragmentDirections.actionSearchFragmentToDetailFragment(film))
        } catch (_: RuntimeException) {
        }
    }

    private fun onItemClick(filmId: String) {
        vm.handleEvent(
            SearchEvent.OnClickFilmItem(filmId),
        )
    }

    private fun EditText.doOnTextUpdated(action: (String) -> Unit) {
        addTextChangedListener(DistinctTextWatcher(action))
    }
}
