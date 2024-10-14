package ru.bratusev.kinopoisk.presentation.decorator

import android.graphics.Color
import androidx.annotation.ColorInt

class Gap(
    @ColorInt val color: Int = Color.parseColor("#150D0B"),
    val width: Int = 0,
    val paddingStart: Int = 0,
    val paddingEnd: Int = 0,
    val isVertical: Boolean = false,
)
