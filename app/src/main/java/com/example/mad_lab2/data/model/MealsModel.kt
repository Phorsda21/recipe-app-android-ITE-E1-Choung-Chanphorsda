package com.example.mad_lab2.data.model


data class MealsModel(
    val area: String? = null,
    val category: String? = null,
    val categoryId: String? = null,
    val id: String? = null,
    val ingredients: List<Ingredient>? = null,
    val instructions: String? = null,
    val meal: String? = null,
    val mealThumb: String? = null,
    val source: String? = null,
    val tags: String? = null,
    val youtube: String? = null
)


data class Ingredient(
    val ingredient: String? = null,
    val measure: String? = null
)