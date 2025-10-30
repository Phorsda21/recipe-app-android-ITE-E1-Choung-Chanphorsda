package com.example.mad_lab2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mad_lab2.data.model.CategoryModel
import com.example.mad_lab2.data.model.MealsModel
import com.example.mad_lab2.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onMealClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit
) {
    val meals = viewModel.meals.collectAsState().value
    val categories = viewModel.categories.collectAsState().value
    val isLoading = viewModel.isLoading.collectAsState().value
    val selectedCategory = remember { mutableStateOf<String?>(null) }

    // Filter meals by selected category
    val filteredMeals = if (selectedCategory.value != null) {
        meals.filter { it.category == selectedCategory.value }
    } else {
        meals
    }

    // Get random meal for suggestion (first 3 meals or available meals)
    val suggestedMeals = if (meals.size >= 10) {
        meals.take(10)
    } else {
        meals
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // App Title
        item {
            Text(
                text = "Recipe Finder",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }

        // Meal Suggestions Section
        if (suggestedMeals.isNotEmpty()) {
            item {
                Text(
                    text = "Meal Suggestions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(suggestedMeals) { meal ->
                        SuggestedMealCard(
                            meal = meal,
                            onClick = { onMealClick(meal.id ?: "") }
                        )
                    }
                }
            }
        }

        // Categories Section
        item {
            Text(
                text = "Categories",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory.value == null,
                        onClick = { selectedCategory.value = null },
                        label = { Text("All") }
                    )
                }
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory.value == category.category,
                        onClick = {
                            selectedCategory.value = category.category
                        },
                        label = { Text(category.category ?: "Unknown") }
                    )
                }
            }
        }

        // Popular Meals Header
        item {
            Text(
                text = "Popular Meals",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (filteredMeals.size > 4) 600.dp else 350.dp)
                ) {
                    items(filteredMeals) { meal ->
                        MealGridCard(
                            meal = meal,
                            onClick = { onMealClick(meal.id ?: "") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestedMealCard(meal: MealsModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = meal.mealThumb,
                contentDescription = meal.meal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = meal.meal ?: "Unknown",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                meal.category?.let { category ->
                    Text(
                        text = category,
                        fontSize = 12.sp,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MealGridCard(meal: MealsModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = meal.mealThumb,
                contentDescription = meal.meal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
            Text(
                text = meal.meal ?: "Unknown",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )
        }
    }
}

@Composable
fun MealCard(meal: MealsModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
                model = meal.mealThumb,
                contentDescription = meal.meal,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
            Text(
                text = meal.meal ?: "Unknown",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )
        }
    }
}

@Composable
fun CategoryCard(category: CategoryModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = category.categoryThumb,
                contentDescription = category.category,
                modifier = Modifier
                    .size(80.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = category.category ?: "Unknown",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )
        }
    }
}