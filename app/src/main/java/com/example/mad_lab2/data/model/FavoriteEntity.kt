package com.example.mad_lab2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val meal: String,
    val mealThumb: String,
    val category: String? = null,
    val area: String? = null,
    val instructions: String? = null,
    val ingredients: String? = null
)