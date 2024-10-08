package ru.bratusev.kinopoisk.presentation.items

data class FrameItemUI(
    val imageUrl: String,
    val previewUrl: String,
) : BaseItem {
    override val itemId: String
        get() = previewUrl
}
