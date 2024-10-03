package ru.bratusev.kinopoisk.presentation.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.bratusev.domain.model.Film
import ru.bratusev.domain.model.Frame
import ru.bratusev.kinopoisk.R

class FrameAdapter :
    RecyclerView.Adapter<FrameAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val frame: ImageView = itemView.findViewById(R.id.image_frame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_frame, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = asyncListDiffer.currentList[position]
        Glide.with(holder.frame)
            .load(currentItem.imageUrl)
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.frame)
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    internal fun setData(data: ArrayList<Frame>) {
        asyncListDiffer.submitList(data)
    }

    private val diffUtil : DiffUtil.ItemCallback<Frame> = object : DiffUtil.ItemCallback<Frame>() {
        override fun areItemsTheSame(oldItem: Frame, newItem: Frame): Boolean = (oldItem === newItem)

        override fun areContentsTheSame(oldItem: Frame, newItem: Frame): Boolean = (oldItem == newItem)
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)
}