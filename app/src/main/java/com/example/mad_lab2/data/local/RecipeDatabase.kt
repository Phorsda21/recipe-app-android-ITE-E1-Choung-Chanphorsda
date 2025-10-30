package com.example.mad_lab2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mad_lab2.data.model.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}