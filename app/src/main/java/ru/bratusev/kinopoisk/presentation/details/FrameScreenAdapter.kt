package ru.bratusev.kinopoisk.presentation.details

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import ru.bratusev.kinopoisk.presentation.details.delegates.frameItemAdapterDelegate
import ru.bratusev.kinopoisk.presentation.items.BaseItem
import ru.bratusev.kinopoisk.presentation.items.itemCallBack

class FrameScreenAdapter : AsyncListDifferDelegationAdapter<BaseItem>(diffUtil()) {
    init {
        delegatesManager.addDelegate(frameItemAdapterDelegate())
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
