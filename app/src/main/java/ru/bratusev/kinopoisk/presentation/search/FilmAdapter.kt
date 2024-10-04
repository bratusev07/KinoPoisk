package ru.bratusev.kinopoisk.presentation.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.bratusev.domain.model.Film
import ru.bratusev.domain.model.Frame
import ru.bratusev.kinopoisk.R

class FilmAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class DefaultViewHolder(holder: View) : RecyclerView.ViewHolder(holder) {
        private val imageFilm: ImageView = itemView.findViewById(R.id.image_film)
        private val textName: TextView = itemView.findViewById(R.id.text_name)
        private val textGenre: TextView = itemView.findViewById(R.id.text_genre)
        private val textDate: TextView = itemView.findViewById(R.id.text_date)
        private val textRating: TextView = itemView.findViewById(R.id.text_rating)

        @SuppressLint("SetTextI18n")
        fun bind(item: ListItem.DefaultItem) {
            val currentItem = item.film
            itemView.setOnClickListener { listener.onItemClick(currentItem) }
            Glide.with(imageFilm)
                .load(currentItem.posterUrlPreview)
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                .into(imageFilm)
            textName.text = currentItem.name
            textGenre.text = currentItem.genre
            textDate.text = "${currentItem.year}, ${currentItem.country}"
            textRating.text = currentItem.ratingKinopoisk.toString()
        }
    }

    inner class YearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val yearOfFilm: TextView = itemView.findViewById(R.id.text_year)

        fun bind(item: ListItem.YearItem) {
            yearOfFilm.text = item.year
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(asyncListDiffer.currentList[position]) {
            is ListItem.DefaultItem -> 0
            is ListItem.YearItem -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> DefaultViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_film, parent, false)
            )
            1 -> YearViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_year_of_film, parent, false)
            )
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = asyncListDiffer.currentList[position]) {
            is ListItem.DefaultItem -> (holder as DefaultViewHolder).bind(item)
            is ListItem.YearItem -> (holder as YearViewHolder).bind(item)
        }
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    fun setData(value: List<ListItem>?) {
        asyncListDiffer.submitList(value)
    }

    private val diffUtil : DiffUtil.ItemCallback<ListItem> = object : DiffUtil.ItemCallback<ListItem>() {
        override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
            return if(oldItem is ListItem.DefaultItem && newItem is ListItem.DefaultItem) oldItem.film.kinopoiskId == newItem.film.kinopoiskId
            else false
        }

        override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean = (oldItem == newItem)
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)
}