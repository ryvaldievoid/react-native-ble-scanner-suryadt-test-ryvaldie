package com.atech.android.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.atech.android.domain.entities.MovieModel

/**
 * Created by Abraham Lay on 27/04/20.
 */
@Database(
    entities = [MovieModel::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
}
