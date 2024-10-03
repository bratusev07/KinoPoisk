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
) :
    RecyclerView.Adapter<FilmAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageFilm: ImageView = itemView.findViewById(R.id.image_film)
        val textName: TextView = itemView.findViewById(R.id.text_name)
        val textGenre: TextView = itemView.findViewById(R.id.text_genre)
        val textDate: TextView = itemView.findViewById(R.id.text_date)
        val textRating: TextView = itemView.findViewById(R.id.text_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_film, parent, false)
        return ItemViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = asyncListDiffer.currentList[position]
        holder.itemView.setOnClickListener { listener.onItemClick(currentItem) }
        Glide.with(holder.imageFilm)
            .load(currentItem.posterUrlPreview)
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.imageFilm)
        holder.textName.text = currentItem.name
        holder.textGenre.text = currentItem.genre
        holder.textDate.text = "${currentItem.year}, ${currentItem.country}"
        holder.textRating.text = currentItem.ratingKinopoisk.toString()
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    fun setData(value: ArrayList<Film>?) {
        asyncListDiffer.submitList(value)
    }

    private val diffUtil : DiffUtil.ItemCallback<Film> = object : DiffUtil.ItemCallback<Film>() {
        override fun areItemsTheSame(oldItem: Film, newItem: Film): Boolean = (oldItem === newItem)

        override fun areContentsTheSame(oldItem: Film, newItem: Film): Boolean = (oldItem == newItem)
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)
}