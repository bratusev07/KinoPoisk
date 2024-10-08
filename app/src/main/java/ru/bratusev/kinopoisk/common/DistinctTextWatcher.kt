package ru.bratusev.kinopoisk.common

import android.annotation.SuppressLint
import android.text.Editable
import com.google.android.material.internal.TextWatcherAdapter

@SuppressLint("RestrictedApi")
class DistinctTextWatcher(
    private val action: (String) -> Unit,
) : TextWatcherAdapter() {
    private var lastValue: String? = null

    override fun afterTextChanged(s: Editable) {
        val content =
            if (s.isEmpty()) {
                ""
            } else {
                s.toString()
            }
        if (lastValue != content) {
            lastValue = content
            action(content)
        }
    }
}
