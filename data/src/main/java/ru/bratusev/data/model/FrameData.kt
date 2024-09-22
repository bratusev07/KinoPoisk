package ru.bratusev.data.model

data class FrameData(
    private val total: Int,
    private val totalPages: Int,
    val items: ArrayList<FrameDTO>
)
