package ru.bratusev.kinopoisk.presentation.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
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
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.domain.model.Frame
import ru.bratusev.kinopoisk.R

class DetailFragment : Fragment() {

    private val vm: DetailViewModel by viewModel()
    private val frameAdapter = FrameScreenAdapter()
    private var webUrl: String? = null

    private lateinit var textDescription: TextView
    private lateinit var textDate: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false).apply {
            configureViews(this)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            loadFilmData()
        }
    }

    private fun loadFilmData() {
        val kinopoiskId = arguments?.getInt("kinopoiskId") ?: 0
        vm.getFilmByIdRemote(kinopoiskId)
        vm.getFramesRemote(kinopoiskId)
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    frameAdapter.items = it.frameList
                    val filmDetail = it.filmDetail
                    textDescription.text = filmDetail.description
                    textDate.text = textDate.text
                    webUrl = filmDetail.webUrl
                }
        }
    }

    private fun configureViews(rootView: View) {
        setupBannerImage(rootView)
        setupTextViews(rootView)
        setupRecyclerView(rootView)
        setupLinkClickListener(rootView)
        setupBackPressHandler()
    }

    private fun setupBannerImage(rootView: View) {
        val bannerImage = rootView.findViewById<ImageView>(R.id.image_banner)
        Glide.with(bannerImage)
            .load(arguments?.getString("banner"))
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .into(bannerImage)
    }

    private fun setupTextViews(rootView: View) {
        rootView.findViewById<TextView>(R.id.text_rating).text = arguments?.getString("rating") ?: "Нет данных"
        rootView.findViewById<TextView>(R.id.text_name).text = arguments?.getString("name") ?: "Нет данных"
        textDescription = rootView.findViewById(R.id.text_description)
        rootView.findViewById<TextView>(R.id.text_genre).text = arguments?.getString("genre") ?: "Нет данных"
        textDate = rootView.findViewById<TextView>(R.id.text_date).apply {
            text = arguments?.getString("date") ?: "Нет данных"
        }
    }

    private fun setupRecyclerView(rootView: View) {
        rootView.findViewById<RecyclerView>(R.id.recycler_frames).apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            adapter = frameAdapter
        }
    }

    private fun setupLinkClickListener(rootView: View) {
        val imageLink = rootView.findViewById<ImageView>(R.id.image_link)
        imageLink.setOnClickListener {
            webUrl?.let {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
            } ?: Toast.makeText(requireContext(), "Ссылка отсутствует или повреждена", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToSearchFragment()
            }
        })
    }

    private fun navigateToSearchFragment() {
        try {
            findNavController().navigate(R.id.action_detailFragment_to_searchFragment)
        } catch (e: RuntimeException) {
            Log.e("DetailFragment", e.message.toString())
        }
    }
}
