package com.app.imagerandom.common

import com.app.imagerandom.domain.model.Genre

object GenreList {
    var genreList: List<Genre> = emptyList()
    fun getGenreById(id: Int): Genre? {
        return genreList.firstOrNull { it.id == id }
    }
}