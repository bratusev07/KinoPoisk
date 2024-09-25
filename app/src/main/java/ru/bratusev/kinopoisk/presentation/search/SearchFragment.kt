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

    private val vm: SearchViewModel by viewModel<SearchViewModel>()
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var textYear: TextView
    private lateinit var inputSearch: TextInputEditText
    private lateinit var progressLoad: ProgressBar

    private val filmAdapter = FilmAdapter(arrayListOf(), this)

    private var order: String = "RATING"
    private var year: String = "1000"
    private var page: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false).also {
            configureViews(it.rootView)
            setObservers()
            NetworkUtils.isInternetAvailable(requireContext())
            vm.getFilmsRemote(order, year, page)
        }
    }

    private fun configureViews(rootView: View) {
        rootView.findViewById<ImageView>(R.id.image_logout).setOnClickListener {
            try {
                findNavController().navigate(R.id.action_searchFragment_to_loginFragment)
            } catch (_: RuntimeException) {
            }
        }

        progressLoad = rootView.findViewById(R.id.progressLoad)
        inputSearch = rootView.findViewById(R.id.input_search)
        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                vm.searchFilms(s.toString())
            }
        })

        rootView.findViewById<ImageView>(R.id.image_sort).setOnClickListener {
            page = 1
            vm.getFilmsRemote(order, year, page, true)
        }

        textYear = rootView.findViewById<TextView>(R.id.yearPicker).also { textView ->
            textView.setOnClickListener {
                val dataPicker = DatePickerDialog(requireContext(), { _, selectedYear, _, _ ->
                    textView.text = selectedYear.toString()
                    year = selectedYear.toString()
                    page = 1
                    vm.getFilmsRemote(order, year, page, true)
                }, 2024, 1, 1)
                dataPicker.datePicker.maxDate = System.currentTimeMillis()
                dataPicker.show()
            }
        }

        swipeRefresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_films).also {
            it.setOnRefreshListener {
                page = 1
                vm.getFilmsRemote(order, year, page, true)
            }
        }

        rootView.findViewById<RecyclerView>(R.id.recycler_films).also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = filmAdapter
            it.addOnScrollListener(onScroll)
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    try {
                        findNavController().navigate(R.id.action_searchFragment_to_loginFragment)
                    } catch (_: RuntimeException) {
                    }
                }
            })
    }

    private val onScroll = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1) {
                if (inputSearch.text.isNullOrEmpty()) vm.getFilmsRemote(order, year, ++page)
            }
        }
    }

    private fun setObservers() {
        vm.filmList.observe(viewLifecycleOwner) {
            filmAdapter.setData(it)
            NetworkUtils.isInternetAvailable(requireContext())
            swipeRefresh.isRefreshing = false
        }

        vm.year.observe(viewLifecycleOwner) {
            textYear.text = it
        }

        vm.isLoading.observe(viewLifecycleOwner) {
            progressLoad.visibility = if(it) ProgressBar.VISIBLE else ProgressBar.INVISIBLE
        }
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
        } catch (_: RuntimeException) {
        }
    }

}