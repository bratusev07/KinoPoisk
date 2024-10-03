package ru.bratusev.kinopoisk.presentation.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.domain.model.Film
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.common.NetworkUtils

class SearchFragment : Fragment(), OnItemClickListener {

    private val vm: SearchViewModel by viewModel()
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var textStartYear: TextView
    private lateinit var textEndYear: TextView
    private lateinit var inputSearch: TextInputEditText
    private lateinit var progressLoad: ProgressBar

    private val filmAdapter = FilmAdapter(this)

    private var order: String = "RATING"
    private var startYear: String = "1000"
    private var endYear: String = "2024"
    private var page: Int = 1

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
            vm.getFilmsRemote(order, startYear, page, endYear)
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
        rootView.findViewById<ImageView>(R.id.image_sort).setOnClickListener {
            page = 1
            order = if(order == "RATING") "YEAR" else "RATING"
            it.rotationY += 180
            vm.getFilmsRemote(order, startYear, page, endYear, true)
        }
    }

    private fun setupYearPicker(rootView: View) {
        textStartYear = rootView.findViewById<TextView>(R.id.startYearPicker).apply {
            setOnClickListener {
                showYearPickerDialog(this)
            }
        }
        textEndYear = rootView.findViewById<TextView>(R.id.endYearPicker).apply {
            setOnClickListener {
                showYearPickerDialog(this, false)
            }
        }
    }

    private fun showYearPickerDialog(textView: TextView, isStart: Boolean = true) {
        val dataPicker = DatePickerDialog(requireContext(), { _, selectedYear, _, _ ->
            val year = selectedYear.toString()
            textView.text = year
            page = 1
            if(isStart) startYear = year
            else endYear = year
            vm.getFilmsRemote(order, startYear, page, endYear,true)
        }, 2024, 1, 1).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }
        dataPicker.show()
    }

    private fun setupSwipeRefresh(rootView: View) {
        swipeRefresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_films).apply {
            setOnRefreshListener {
                page = 1
                vm.getFilmsRemote(order, startYear, page, endYear, true)
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
                if (inputSearch.text.isNullOrEmpty()) {
                    vm.getFilmsRemote(order, startYear, ++page, endYear)
                } else Toast.makeText(requireContext(),
                    "Это все ответы найденые локально и удовлетворяющие вашему запросу",
                    Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setObservers() {
        vm.filmList.observe(viewLifecycleOwner) {
            val filmList = mutableListOf<ListItem>()
            val seenYears = mutableSetOf<Int>()
            it.forEach { film ->
                val year = film.year
                if (year !in seenYears) {
                    filmList.add(ListItem.YearItem(year.toString()))
                    seenYears.add(year)
                }
                filmList.add(ListItem.DefaultItem(film))
            }
            filmAdapter.setData(filmList)
            swipeRefresh.isRefreshing = false
        }

        vm.isLoading.observe(viewLifecycleOwner) {
            progressLoad.visibility = if (it) ProgressBar.VISIBLE else ProgressBar.INVISIBLE
        }
    }

    private fun navigateToLoginFragment() {
        try {
            findNavController().navigate(R.id.action_searchFragment_to_loginFragment)
        } catch (_: RuntimeException) { }
    }

    override fun onItemClick(film: Film) {
        val bundle = Bundle().apply {
            putString("banner", film.posterUrl)
            putString("rating", film.ratingKinopoisk.toString())
            putString("name", film.name)
            putInt("kinopoiskId", film.kinopoiskId)
            putString("genre", film.genre)
            putString("country", film.country)
        }
        try {
            findNavController().navigate(R.id.action_searchFragment_to_detailFragment, bundle)
        } catch (_: RuntimeException) { }
    }
}