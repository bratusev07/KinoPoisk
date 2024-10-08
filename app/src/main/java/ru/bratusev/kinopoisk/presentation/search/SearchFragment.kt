package ru.bratusev.kinopoisk.presentation.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.common.NetworkUtils
import ru.bratusev.kinopoisk.databinding.FragmentSearchBinding
import ru.bratusev.kinopoisk.presentation.items.FilmItemUI

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val vm: SearchViewModel by viewModel()
    private val viewBinding: FragmentSearchBinding by viewBinding()

    private val filmAdapter = FilmAdapter { film ->
        onItemClick(film)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        setObservers()
        loadInitialData()
    }

    private fun loadInitialData() {
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            vm.getFilmsRemote()
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
            navigateToLoginFragment()
        }
    }

    private fun setupSearchInput() {
        viewBinding.inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                vm.searchFilms(s.toString())
            }
        })
    }

    private fun setupSortButton() {
        viewBinding.imageSort.setOnClickListener {
            vm.sortFilms()
        }
    }

    private fun setupYearPicker() {
        viewBinding.startYearPicker.setOnClickListener {
            showYearPickerDialog()
        }
        viewBinding.endYearPicker.setOnClickListener {
            showYearPickerDialog(false)
        }
    }

    private fun showYearPickerDialog(isStart: Boolean = true) {
        val dataPicker = DatePickerDialog(requireContext(), { _, selectedYear, _, _ ->
            vm.getFilmsByYear(selectedYear.toString(), isStart)
        }, 2024, 1, 1).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }
        dataPicker.show()
    }

    private fun setupSwipeRefresh() {
        viewBinding.swipeRefreshFilms.setOnRefreshListener {
            vm.refreshFilms()
        }
    }

    private fun setupRecyclerView() {
        viewBinding.recyclerFilms.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = filmAdapter
            addOnScrollListener(onScroll)
        }
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navigateToLoginFragment()
                }
            }
        )
    }

    private val onScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                if (viewBinding.inputSearch.text.isNullOrEmpty()) vm.getNextPage()
                else Toast.makeText(requireContext(),
                    "Это все ответы найденые локально и удовлетворяющие вашему запросу",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    filmAdapter.items = it.filmList
                    with(viewBinding){
                        swipeRefreshFilms.isRefreshing = it.isRefreshing
                        progressLoad.visibility = if (it.isLoading) ProgressBar.VISIBLE else ProgressBar.INVISIBLE
                        imageSort.rotationY = it.rotationSortIcon
                        startYearPicker.text = it.params.year
                        endYearPicker.text = it.params.endYear
                    }
                }
        }
    }

    private fun navigateToLoginFragment() {
        try {
            findNavController().navigate(R.id.action_searchFragment_to_loginFragment)
        } catch (_: RuntimeException) { }
    }

    private fun onItemClick(film: FilmItemUI) {
        val bundle = Bundle().apply {
            putString("banner", film.posterUrl)
            putString("rating", film.ratingKinopoisk.toString())
            putString("name", film.name)
            putInt("kinopoiskId", film.itemId.toInt())
            putString("genre", film.genre)
            putString("date", film.date)
        }
        try {
            findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
        } catch (_: RuntimeException) { }
    }
}