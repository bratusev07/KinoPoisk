package ru.bratusev.kinopoisk.presentation.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.databinding.FragmentDetailBinding
import ru.bratusev.kinopoisk.presentation.decorator.Gap
import ru.bratusev.kinopoisk.presentation.decorator.ItemDividerDrawer
import ru.bratusev.kinopoisk.presentation.items.FilmArgs
import ru.surfstudio.android.recycler.decorator.Decorator

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val vm by viewModels<DetailViewModel>()

    private val viewBinding: FragmentDetailBinding by viewBinding()
    private val frameAdapter = FrameScreenAdapter()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        configureViews()
        loadFilmData()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    private fun loadFilmData() {
        vm.handleEvent(
            DetailEvent.OnFragmentStart,
        )
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    frameAdapter.items = it.frameList
                    viewBinding.textDescription.text = it.filmDetail.description
                    it.webUrl = it.filmDetail.webUrl
                    setupTextViews(it.film)
                    setupBannerImage(it.film.posterUrlPreview)
                }
        }
        vm.uiLabels.observe(viewLifecycleOwner) {
            when (it) {
                is DetailLabel.OpenUrl -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it.webUrl)))
                is DetailLabel.GoToPrevious -> navigateToSearchFragment()
            }
        }
    }

    private fun configureViews() {
        setupRecyclerView()
        setupLinkClickListener()
        setupBackPressHandler()
    }

    private fun setupBannerImage(imageUrl: String) {
        Glide
            .with(viewBinding.imageBanner)
            .load(imageUrl)
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .into(viewBinding.imageBanner)
    }

    private fun setupTextViews(film: FilmArgs) {
        with(viewBinding) {
            textRating.text = film.ratingKinopoisk.toString()
            textName.text = film.name
            textGenre.text = film.genre
            textDate.text = film.country
        }
    }

    private fun setupRecyclerView() {
        viewBinding.recyclerFrames.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = frameAdapter
            addItemDecoration(frameItemUIDecorator)
        }
    }

    private fun setupLinkClickListener() {
        viewBinding.imageLink.setOnClickListener {
            vm.uiState.value.webUrl.let {
                vm.handleEvent(DetailEvent.OnClickWebUrl(it))
            }
        }
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    vm.handleEvent(DetailEvent.OnClickBack)
                }
            },
        )
    }

    private fun navigateToSearchFragment() {
        try {
            findNavController().navigate(R.id.action_detailFragment_to_searchFragment)
        } catch (e: RuntimeException) {
            Log.e("DetailFragment", e.message.toString())
        }
    }
}
