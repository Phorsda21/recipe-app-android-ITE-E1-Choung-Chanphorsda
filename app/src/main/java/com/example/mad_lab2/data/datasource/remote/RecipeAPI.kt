package com.example.mad_lab2.data.datasource.remote

import com.example.mad_lab2.data.model.CategoryModel
import com.example.mad_lab2.data.model.MealsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeAPI {
    @GET("/meals")
    suspend fun fetchMeals(
        @Query("_page") page: Int = 1,
        @Query("_limit") limit: Int = 15,
        @Query("categoryId") categoryId: String? = null,
        @Query("id") id: String? = null,
        @Query("area") area: String? = null
    ): List<MealsModel>

    @GET("/categories")
    suspend fun fetchCategory(): List<CategoryModel>
}