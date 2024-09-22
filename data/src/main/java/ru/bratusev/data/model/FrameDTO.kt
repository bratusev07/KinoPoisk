package ru.bratusev.data.model

import ru.bratusev.domain.model.Frame

data class FrameDTO(
    internal val imageUrl: String?,
    internal val previewUrl: String?
)

fun FrameDTO.toFrame(): Frame {
    return Frame(
        imageUrl = imageUrl ?: "нет данных",
        previewUrl = previewUrl ?: "нет данных"
    )
}