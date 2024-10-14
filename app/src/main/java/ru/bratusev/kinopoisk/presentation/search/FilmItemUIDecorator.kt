package ru.bratusev.kinopoisk.presentation.search

import ru.bratusev.kinopoisk.presentation.decorator.Gap
import ru.bratusev.kinopoisk.presentation.decorator.ItemDividerDrawer
import ru.surfstudio.android.recycler.decorator.Decorator

internal val filmItemUIDecorator by lazy {
    Decorator.Builder()
        .overlay(ItemDividerDrawer(Gap(width = 40, isVertical = true)))
        .build()
}