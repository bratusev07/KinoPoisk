package ru.bratusev.kinopoisk.presentation.mapper

import ru.bratusev.domain.model.Frame
import ru.bratusev.kinopoisk.presentation.items.BaseItem
import ru.bratusev.kinopoisk.presentation.items.FrameItemUI

class DetailScreenMapper : Mapper<List<Frame>, List<BaseItem>> {

    override fun transform(data: List<Frame>): List<BaseItem> {
        return data.map { mapToFrameUI(it) }
    }

    private fun mapToFrameUI(frame: Frame): FrameItemUI = FrameItemUI(frame.imageUrl, frame.previewUrl)

}