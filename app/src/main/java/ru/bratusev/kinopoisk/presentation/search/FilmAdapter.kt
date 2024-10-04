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
import ru.bratusev.kinopoisk.R

class FilmAdapter(
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class FilmViewHolder(holder: View) : RecyclerView.ViewHolder(holder) {
        private val imageFilm: ImageView = itemView.findViewById(R.id.image_film)
        private val textName: TextView = itemView.findViewById(R.id.text_name)
        private val textGenre: TextView = itemView.findViewById(R.id.text_genre)
        private val textDate: TextView = itemView.findViewById(R.id.text_date)
        private val textRating: TextView = itemView.findViewById(R.id.text_rating)

        @SuppressLint("SetTextI18n")
        fun bind(item: BaseItem.FilmItemUI) {
            itemView.setOnClickListener { listener.onItemClick(item) }
            Glide.with(imageFilm)
                .load(item.posterUrlPreview)
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                .into(imageFilm)
            textName.text = item.name
            textGenre.text = item.genre
            textDate.text = "${item.year}, ${item.country}"
            textRating.text = item.ratingKinopoisk.toString()
        }
    }

    inner class YearViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val yearOfFilm: TextView = itemView.findViewById(R.id.text_year)

        fun bind(item: BaseItem.YearItemUI) {
            yearOfFilm.text = item.year
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(asyncListDiffer.currentList[position]) {
            is BaseItem.FilmItemUI -> 0
            is BaseItem.YearItemUI -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> FilmViewHolder(
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
            is BaseItem.FilmItemUI -> (holder as FilmViewHolder).bind(item)
            is BaseItem.YearItemUI -> (holder as YearViewHolder).bind(item)
        }
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    fun setData(value: List<BaseItem>?) {
        asyncListDiffer.submitList(value)
    }

    private val diffUtil : DiffUtil.ItemCallback<BaseItem> = object : DiffUtil.ItemCallback<BaseItem>() {
        override fun areItemsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean {
            return oldItem.itemId  == newItem.itemId
        }

        override fun areContentsTheSame(oldItem: BaseItem, newItem: BaseItem): Boolean = (oldItem == newItem)
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)
}