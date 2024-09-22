package ru.bratusev.kinopoisk.presentation.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.bratusev.domain.model.Frame
import ru.bratusev.kinopoisk.R

class FrameAdapter(private var frameList: List<Frame>) :
    RecyclerView.Adapter<FrameAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val frame: ImageView = itemView.findViewById(R.id.image_frame)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_frame, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = frameList[position]
        Glide.with(holder.frame)
            .load(currentItem.imageUrl)
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .into(holder.frame)
    }

    override fun getItemCount() = frameList.size

    @SuppressLint("NotifyDataSetChanged")
    internal fun setData(data: ArrayList<Frame>) {
        frameList = data
        notifyDataSetChanged()
    }
}