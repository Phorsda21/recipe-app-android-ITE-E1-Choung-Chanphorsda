package com.example.mad_lab2.data.local

import androidx.room.*
import com.example.mad_lab2.data.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(favorite: FavoriteEntity)

    @Delete
    suspend fun removeFavorite(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorites")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites WHERE id = :id")
    suspend fun getFavoriteById(id: String): FavoriteEntity?

    @Query("SELECT COUNT(*) FROM favorites WHERE id = :id")
    suspend fun isFavorite(id: String): Int
}