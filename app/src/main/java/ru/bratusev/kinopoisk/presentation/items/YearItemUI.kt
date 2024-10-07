package ru.bratusev.kinopoisk.presentation.items

data class YearItemUI(
    override val itemId: String,
    val year: String
) : BaseItem