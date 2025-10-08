package com.app.imagerandom.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.imagerandom.domain.model.Genre

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey val id: Int,
    val name: String
) {
    fun toDomain() = Genre(id, name)

    companion object {
        fun fromDomain(genre: Genre) = GenreEntity(genre.id, genre.name)
    }
}
