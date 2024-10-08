package ru.bratusev.kinopoisk.presentation.search.delegates

import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.databinding.ItemFilmBinding
import ru.bratusev.kinopoisk.presentation.items.BaseItem
import ru.bratusev.kinopoisk.presentation.items.FilmItemUI

fun filmItemAdapterDelegates(onItemClick: (FilmItemUI) -> Unit) =
    adapterDelegateViewBinding<FilmItemUI, BaseItem, ItemFilmBinding>(
        { layoutInflater, root -> ItemFilmBinding.inflate(layoutInflater, root, false) },
    ) {
        itemView.setOnClickListener { onItemClick(item) }
        bind {
            with(binding) {
                Glide
                    .with(imageFilm)
                    .load(item.posterUrlPreview)
                    .error(R.drawable.ic_placeholder)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imageFilm)
                textName.text = item.name
                textGenre.text = item.genre
                textDate.text = item.date
                textRating.text = item.ratingKinopoisk.toString()
            }
        }
    }
