package ru.bratusev.kinopoisk.presentation.mapper

interface Mapper<SRC, DST> {
    fun transform(data: SRC): DST
}
