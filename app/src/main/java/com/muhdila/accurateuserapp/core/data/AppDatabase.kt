package com.muhdila.accurateuserapp.core.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.muhdila.accurateuserapp.user.data.local.dao.UserDao
import com.muhdila.accurateuserapp.user.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
