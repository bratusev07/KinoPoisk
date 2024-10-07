package ru.bratusev.kinopoisk.presentation.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.common.NetworkUtils
import ru.bratusev.kinopoisk.presentation.items.FilmItemUI

class SearchFragment : Fragment() {

    private val vm: SearchViewModel by viewModel()
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var imageSortFilms: ImageView
    private lateinit var textStartYear: TextView
    private lateinit var textEndYear: TextView
    private lateinit var inputSearch: TextInputEditText
    private lateinit var progressLoad: ProgressBar

    private val filmAdapter = FilmAdapter { film ->
        onItemClick(film)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false).apply {
            configureViews(this)
            setObservers()
            loadInitialData()
        }
    }

    private fun loadInitialData() {
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            vm.getFilmsRemote()
        }
    }

    private fun configureViews(rootView: View) {
        setupLogoutButton(rootView)
        setupProgressBar(rootView)
        setupSearchInput(rootView)
        setupSortButton(rootView)
        setupYearPicker(rootView)
        setupSwipeRefresh(rootView)
        setupRecyclerView(rootView)
        setupBackPressHandler()
    }

    private fun setupLogoutButton(rootView: View) {
        rootView.findViewById<ImageView>(R.id.image_logout).setOnClickListener {
            navigateToLoginFragment()
        }
    }

    private fun setupProgressBar(rootView: View) {
        progressLoad = rootView.findViewById(R.id.progressLoad)
    }

    private fun setupSearchInput(rootView: View) {
        inputSearch = rootView.findViewById(R.id.input_search)
        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                vm.searchFilms(s.toString())
            }
        })
    }

    private fun setupSortButton(rootView: View) {
        imageSortFilms = rootView.findViewById<ImageView>(R.id.image_sort).apply {
            setOnClickListener {
                vm.sortFilms()
            }
        }
    }

    private fun setupYearPicker(rootView: View) {
        textStartYear = rootView.findViewById<TextView>(R.id.startYearPicker).apply {
            setOnClickListener {
                showYearPickerDialog()
            }
        }
        textEndYear = rootView.findViewById<TextView>(R.id.endYearPicker).apply {
            setOnClickListener {
                showYearPickerDialog( false)
            }
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

    private fun setupSwipeRefresh(rootView: View) {
        swipeRefresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_films).apply {
            setOnRefreshListener {
                vm.refreshFilms()
            }
        }
    }

    private fun setupRecyclerView(rootView: View) {
        rootView.findViewById<RecyclerView>(R.id.recycler_films).apply {
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
                if (inputSearch.text.isNullOrEmpty()) vm.getNextPage()
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
                    swipeRefresh.isRefreshing = it.isRefreshing
                    progressLoad.visibility = if (it.isLoading) ProgressBar.VISIBLE else ProgressBar.INVISIBLE
                    imageSortFilms.rotationY = it.rotationSortIcon
                    textStartYear.text = it.params.year
                    textEndYear.text = it.params.endYear
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