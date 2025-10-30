package com.example.mad_lab2.data.repository

import com.example.mad_lab2.data.datasource.remote.RecipeAPI
import com.example.mad_lab2.data.local.FavoriteDao
import com.example.mad_lab2.data.model.CategoryModel
import com.example.mad_lab2.data.model.FavoriteEntity
import com.example.mad_lab2.data.model.MealsModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeAPI: RecipeAPI,
    private val favoriteDao: FavoriteDao
) {
    suspend fun fetchMeals(
        page: Int = 1,
        categoryId: String? = null,
        area: String? = null
    ): List<MealsModel> = recipeAPI.fetchMeals(
        page = page,
        categoryId = categoryId,
        area = area
    )

    suspend fun fetchCategories(): List<CategoryModel> = recipeAPI.fetchCategory()

    suspend fun addFavorite(favorite: FavoriteEntity) = favoriteDao.addFavorite(favorite)

    suspend fun removeFavorite(favorite: FavoriteEntity) = favoriteDao.removeFavorite(favorite)

    fun getAllFavorites(): Flow<List<FavoriteEntity>> = favoriteDao.getAllFavorites()

    suspend fun isFavorite(id: String): Boolean = favoriteDao.isFavorite(id) > 0
}