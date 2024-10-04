package ru.bratusev.kinopoisk.presentation.items

import androidx.recyclerview.widget.DiffUtil

fun <T : Any> itemCallBack(
    areItemsTheSame: (oldItem: T, newItem: T) -> Boolean,
    areContentsTheSame: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem ->
        oldItem == newItem
    },
    getChangePayload: (oldItem: T, newItem: T) -> Any? = { _, _ ->
        null
    }
) = object : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = areItemsTheSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = areContentsTheSame(oldItem, newItem)

    override fun getChangePayload(oldItem: T, newItem: T): Any? = getChangePayload(oldItem, newItem)
}