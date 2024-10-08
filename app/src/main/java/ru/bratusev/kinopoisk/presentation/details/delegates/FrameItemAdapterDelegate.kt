package ru.bratusev.kinopoisk.presentation.details.delegates

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.databinding.ItemFrameBinding
import ru.bratusev.kinopoisk.presentation.items.BaseItem
import ru.bratusev.kinopoisk.presentation.items.FrameItemUI

fun frameItemAdapterDelegate() =
    adapterDelegateViewBinding<FrameItemUI, BaseItem, ItemFrameBinding>(
        { layoutInflater, root -> ItemFrameBinding.inflate(layoutInflater, root, false) },
    ) {
        bind {
            Glide
                .with(binding.imageFrame)
                .load(item.imageUrl)
                .error(R.drawable.ic_placeholder)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.imageFrame)
        }
    }
