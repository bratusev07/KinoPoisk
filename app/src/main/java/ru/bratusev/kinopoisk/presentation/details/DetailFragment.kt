package ru.bratusev.kinopoisk.presentation.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.databinding.FragmentDetailBinding

class DetailFragment : Fragment(R.layout.fragment_detail) {
    private val vm: DetailViewModel by viewModel()
    private val viewBinding: FragmentDetailBinding by viewBinding()
    private val frameAdapter = FrameScreenAdapter()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        configureViews()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        loadFilmData()
    }

    private fun loadFilmData() {
        vm.handleEvent(DetailEvent.OnFragmentStart(arguments?.getInt("kinopoiskId") ?: 0))
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    frameAdapter.items = it.frameList
                    viewBinding.textDescription.text = it.filmDetail.description
                    it.webUrl = it.filmDetail.webUrl
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
        setupBannerImage()
        setupTextViews()
        setupRecyclerView()
        setupLinkClickListener()
        setupBackPressHandler()
    }

    private fun setupBannerImage() {
        Glide
            .with(viewBinding.imageBanner)
            .load(arguments?.getString("banner"))
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .into(viewBinding.imageBanner)
    }

    private fun setupTextViews() {
        with(viewBinding) {
            textRating.text = arguments?.getString("rating") ?: "Нет данных"
            textName.text = arguments?.getString("name") ?: "Нет данных"
            textGenre.text = arguments?.getString("genre") ?: "Нет данных"
            textDate.text = arguments?.getString("date") ?: "Нет данных"
        }
    }

    private fun setupRecyclerView() {
        viewBinding.recyclerFrames.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = frameAdapter
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
