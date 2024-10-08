package ru.bratusev.kinopoisk.presentation.search

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.bratusev.kinopoisk.presentation.items.BaseItem
import ru.bratusev.kinopoisk.presentation.items.FilmItemUI
import ru.bratusev.kinopoisk.presentation.items.itemCallBack
import ru.bratusev.kinopoisk.presentation.search.delegates.filmItemAdapterDelegates
import ru.bratusev.kinopoisk.presentation.search.delegates.yearItemAdapterDelegate

class FilmAdapter(
    onItemClick: (FilmItemUI) -> Unit,
) : AsyncListDifferDelegationAdapter<BaseItem>(diffUtil()) {
    init {
        delegatesManager.addDelegate(filmItemAdapterDelegates(onItemClick))
        delegatesManager.addDelegate(yearItemAdapterDelegate())
    }
}

private fun diffUtil() =
    itemCallBack<BaseItem>(
        areItemsTheSame = { oldItem, newItem -> oldItem.itemId == newItem.itemId },
        areContentsTheSame = { oldItem, newItem -> oldItem == newItem },
        getChangePayload = { _, _ ->
            Any()
        },
    )
