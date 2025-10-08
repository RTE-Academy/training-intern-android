package com.app.imagerandom.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.imagerandom.data.local.entity.GenreEntity

@Dao
interface GenreDao {

    @Query("SELECT * FROM genres")
    fun getAllGenres(): List<GenreEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(genres: List<GenreEntity>)

    @Query("DELETE FROM genres")
    suspend fun clearAll()
}
