package com.example.mad_lab2.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mad_lab2.data.model.MealsModel
import com.example.mad_lab2.ui.screen.*
import com.example.mad_lab2.ui.viewmodel.*

@Composable
fun RecipeNavigation() {
    val navController = rememberNavController()
    val startDestination = remember { mutableStateOf(Screens.Onboarding.route) }
    val selectedScreen = remember { mutableStateOf(Screens.Home.route) }

    // Store current meal for detail screen
    val currentMeal = remember { mutableStateOf<MealsModel?>(null) }

    Scaffold(
        bottomBar = {
            if (startDestination.value != Screens.Onboarding.route &&
                navController.currentDestination?.route != Screens.Detail.route &&
                !navController.currentDestination?.route?.startsWith("detail")!!) {
                BottomNavigationBar(
                    selectedScreen = selectedScreen.value,
                    onNavigate = { route ->
                        selectedScreen.value = route
                        navController.navigate(route) {
                            popUpTo(Screens.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination.value,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screens.Onboarding.route) {
                OnboardingScreen {
                    startDestination.value = Screens.Home.route
                    navController.navigate(Screens.Home.route) {
                        popUpTo(Screens.Onboarding.route) { inclusive = true }
                    }
                }
            }

            composable(Screens.Home.route) {
                val homeViewModel: HomeViewModel = hiltViewModel()
                HomeScreen(
                    viewModel = homeViewModel,
                    onMealClick = { mealId ->
                        // Find the meal from the ViewModel
                        val meals = homeViewModel.meals.value
                        val selectedMeal = meals.find { it.id == mealId }
                        currentMeal.value = selectedMeal
                        navController.navigate(Screens.Detail.createRoute(mealId))
                    },
                    onCategoryClick = { categoryId ->
                        navController.navigate(Screens.Explore.route) {
                            popUpTo(Screens.Home.route) { saveState = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screens.Explore.route) {
                val exploreViewModel: ExploreViewModel = hiltViewModel()
                ExploreScreen(
                    viewModel = exploreViewModel,
                    onMealClick = { mealId ->
                        val meals = exploreViewModel.meals.value
                        val selectedMeal = meals.find { it.id == mealId }
                        currentMeal.value = selectedMeal
                        navController.navigate(Screens.Detail.createRoute(mealId))
                    }
                )
            }

            composable(Screens.Favorite.route) {
                val favoriteViewModel: FavoriteViewModel = hiltViewModel()
                FavoriteScreen(
                    viewModel = favoriteViewModel,
                    onMealClick = { mealId ->
                        // For favorites, we need to construct a meal from favorite entity
                        navController.navigate(Screens.Detail.createRoute(mealId))
                    }
                )
            }

            composable(
                Screens.Detail.route,
                arguments = listOf(
                    navArgument("mealId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val mealId = backStackEntry.arguments?.getString("mealId")

                DetailScreen(
                    mealId = mealId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    selectedScreen: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, "Home") },
            label = { Text("Home") },
            selected = selectedScreen == Screens.Home.route,
            onClick = { onNavigate(Screens.Home.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, "Explore") },
            label = { Text("Explore") },
            selected = selectedScreen == Screens.Explore.route,
            onClick = { onNavigate(Screens.Explore.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Favorite, "Favorite") },
            label = { Text("Favorite") },
            selected = selectedScreen == Screens.Favorite.route,
            onClick = { onNavigate(Screens.Favorite.route) }
        )
    }
}