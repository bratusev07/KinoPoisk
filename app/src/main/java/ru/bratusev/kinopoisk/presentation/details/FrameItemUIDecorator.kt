package ru.bratusev.kinopoisk.presentation.details

import ru.bratusev.kinopoisk.presentation.decorator.Gap
import ru.bratusev.kinopoisk.presentation.decorator.ItemDividerDrawer
import ru.surfstudio.android.recycler.decorator.Decorator

internal val frameItemUIDecorator by lazy {
    Decorator.Builder()
        .overlay(ItemDividerDrawer(Gap(width = 40)))
        .build()
}