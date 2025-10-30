package com.example.mad_lab2.ui.navigation

sealed class Screens(val route: String) {
    object Onboarding : Screens("onboarding")
    object Home : Screens("home")
    object Explore : Screens("explore")
    object Favorite : Screens("favorite")
    object Detail : Screens("detail/{mealId}") {
        fun createRoute(mealId: String) = "detail/$mealId"
    }
}