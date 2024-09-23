package ru.bratusev.kinopoisk.presentation.search

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
    private val filmAdapter = FilmAdapter(arrayListOf(), this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false).also {
            configureViews(it.rootView)
            setObservers()
            NetworkUtils.isInternetAvailable(requireContext())
            vm.getFilmsRemote()
        }
    }

    private fun configureViews(rootView: View) {
        rootView.findViewById<ImageView>(R.id.image_logout).setOnClickListener {
            vm.logout()
            try {
                findNavController().navigate(R.id.action_searchFragment_to_loginFragment)
            } catch (_: RuntimeException) {
            }
        }

        rootView.findViewById<TextInputEditText>(R.id.input_search).addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                vm.searchFilms(s.toString())
            }
        })

        rootView.findViewById<ImageView>(R.id.image_sort).setOnClickListener { vm.sortFilms() }

        textYear = rootView.findViewById<TextView>(R.id.yearPicker).also { textView ->
            textView.setOnClickListener {
                DatePickerDialog(requireContext(), { _, selectedYear, _, _ ->
                    textView.text = selectedYear.toString()
                }, 2024, 1, 1).show()
            }
        }

        swipeRefresh = rootView.findViewById<SwipeRefreshLayout>(R.id.swipe_refresh_films)
            .also { it.setOnRefreshListener { vm.getFilmsRemote() } }

        rootView.findViewById<RecyclerView>(R.id.recycler_films).also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.adapter = filmAdapter
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

    private fun setObservers() {
        vm.filmList.observe(viewLifecycleOwner) {
            filmAdapter.setData(it)
            NetworkUtils.isInternetAvailable(requireContext())
            swipeRefresh.isRefreshing = false
        }

        vm.year.observe(viewLifecycleOwner) {
            textYear.text = it
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