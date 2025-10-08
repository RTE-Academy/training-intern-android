package com.app.imagerandom.domain.usecase.genres

import com.app.imagerandom.common.GenreList
import com.app.imagerandom.data.repository.genre.GenreRepository
import com.app.imagerandom.domain.model.Genre
import javax.inject.Inject

class GenreUseCase @Inject constructor(
    private val repo: GenreRepository
) {
    // Luu danh sach the loai phim
    suspend fun saveMovieGenreList(genres: List<Genre>) = repo.saveGenres(genres)

    // Lay danh sach the loai phim
    fun getAllGenres() {
        GenreList.genreList = repo.getAllGenres()
    }
}