package com.app.imagerandom.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.imagerandom.data.local.dao.GenreDao
import com.app.imagerandom.data.local.entity.GenreEntity

@Database(
    entities = [GenreEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun genreDao(): GenreDao
}