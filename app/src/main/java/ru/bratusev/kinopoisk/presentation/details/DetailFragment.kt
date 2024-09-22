package ru.bratusev.kinopoisk.presentation.details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.common.NetworkUtils

class DetailFragment : Fragment() {

    private val vm: DetailViewModel by viewModel<DetailViewModel>()
    private val frameAdapter = FrameAdapter(arrayListOf())
    private var webUrl: String? = null

    private lateinit var textDescription: TextView
    private lateinit var textDate: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false).also {
            configureViews(it.rootView)
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            NetworkUtils.isInternetAvailable(requireContext())
            setObservers()
            vm.getFilmByIdRemote(arguments?.getInt("kinopoiskId") ?: 0)
            vm.getFramesRemote(arguments?.getInt("kinopoiskId") ?: 0)
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setObservers() {
        vm.frameList.observe(viewLifecycleOwner) {
            frameAdapter.setData(it)
        }

        vm.filmDetail.observe(viewLifecycleOwner) {
            textDescription.text = it.description
            textDate.text = "${it.startYear}-${it.endYear}, ${textDate.text}"
            webUrl = it.webUrl
        }
    }

    private fun configureViews(rootView: View) {
        rootView.findViewById<ImageView>(R.id.image_banner).also {
            Glide.with(it).load(arguments?.getString("banner"))
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                .into(it)
        }
        rootView.findViewById<TextView>(R.id.text_rating).also {
            it.text = arguments?.getString("rating") ?: "Нет данных"
        }
        val imageLink = rootView.findViewById<ImageView>(R.id.image_link)
        imageLink.setOnClickListener {
            if (webUrl != null) {
                val intent = Intent(Intent.ACTION_VIEW).also { it.data = Uri.parse(webUrl) }
                startActivity(intent)
            } else Toast.makeText(
                requireContext(),
                "Ссылка отсутствует или повреждена",
                Toast.LENGTH_SHORT
            ).show()
        }
        rootView.findViewById<TextView>(R.id.text_name).also {
            it.text = arguments?.getString("name") ?: "Нет данных"
        }
        textDescription = rootView.findViewById(R.id.text_description)
        rootView.findViewById<TextView>(R.id.text_genre).also {
            it.text = arguments?.getString("genre") ?: "Нет данных"
        }
        textDate = rootView.findViewById<TextView>(R.id.text_date).also {
            it.text = arguments?.getString("country") ?: "Нет данных"
        }
        rootView.findViewById<RecyclerView>(R.id.recycler_frames).also {
            it.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            it.adapter = frameAdapter
        }
    }
}