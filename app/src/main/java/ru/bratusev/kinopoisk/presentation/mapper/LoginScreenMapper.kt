package ru.bratusev.kinopoisk.presentation.mapper

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.bratusev.kinopoisk.R
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class LoginScreenMapper
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : Mapper<LocalDateTime, String> {
        override fun transform(data: LocalDateTime): String {
            val now = LocalDateTime.now()
            val daysAgo = ChronoUnit.DAYS.between(data, now).toInt()
            if (daysAgo > 0) return mapDaysToString(daysAgo)
            val hoursAgo = ChronoUnit.HOURS.between(data, now).toInt()
            if (hoursAgo > 0) return mapHoursToString(hoursAgo)
            return mapMinutesToString(ChronoUnit.MINUTES.between(data, now).toInt())
        }

        private fun mapMinutesToString(minutes: Int): String =
            when {
                minutes < 1 -> "Последний вход только что"
                minutes < 60 -> context.resources.getQuantityString(R.plurals.minutes, minutes, minutes)
                else -> ""
            }

        private fun mapHoursToString(hours: Int): String =
            when {
                hours < 24 -> context.resources.getQuantityString(R.plurals.minutes, hours, hours)
                else -> ""
            }

        private fun mapDaysToString(days: Int): String = context.resources.getQuantityString(R.plurals.days, days, days)
    }
