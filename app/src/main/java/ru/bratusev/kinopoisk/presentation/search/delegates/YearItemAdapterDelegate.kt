package ru.bratusev.kinopoisk.presentation.search.delegates

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.bratusev.kinopoisk.databinding.ItemYearOfFilmBinding
import ru.bratusev.kinopoisk.presentation.items.BaseItem
import ru.bratusev.kinopoisk.presentation.items.YearItemUI

fun yearItemAdapterDelegate() =
    adapterDelegateViewBinding<YearItemUI, BaseItem, ItemYearOfFilmBinding>(
        { layoutInflater, root -> ItemYearOfFilmBinding.inflate(layoutInflater, root, false) },
    ) {
        bind {
            binding.textYear.text = item.year
        }
    }
