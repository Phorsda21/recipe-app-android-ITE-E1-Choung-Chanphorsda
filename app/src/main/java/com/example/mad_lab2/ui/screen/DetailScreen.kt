package com.example.mad_lab2.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mad_lab2.ui.viewmodel.DetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    mealId: String?,
    onNavigateBack: () -> Unit
) {
    val viewModel: DetailViewModel = hiltViewModel()
    val viewState = viewModel.uiState.collectAsState()
    val state = viewState.value

    if (state.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Text(
                    "Loading recipe...",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Recipe Details") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        viewModel.toggleFavorite()
                    }
                ) {
                    Icon(
                        imageVector = if (state.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (state.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                AsyncImage(
                    model = state.meal.mealThumb,
                    contentDescription = state.meal.meal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                Text(
                    text = state.meal.meal ?: "Unknown",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    ElevatedCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Category", fontSize = 11.sp)
                            Text(state.meal.category ?: "N/A", fontWeight = FontWeight.Bold)
                        }
                    }
                    ElevatedCard(modifier = Modifier.weight(1f)) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Area", fontSize = 11.sp)
                            Text(state.meal.area ?: "N/A", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Ingredients",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            items(state.meal.ingredients ?: emptyList()) { ingredient ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("•", fontSize = 14.sp)
                    Column(modifier = Modifier.weight(1f)) {
                        Text(ingredient.ingredient ?: "", fontWeight = FontWeight.SemiBold)
                        Text(ingredient.measure ?: "", fontSize = 12.sp)
                    }
                }
            }

            item {
                Text(
                    text = "Instructions",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                Text(
                    text = state.meal.instructions ?: "No instructions available",
                    fontSize = 14.sp
                )
            }

            if (!state.meal.youtube.isNullOrBlank()) {
                item {
                    ElevatedButton(
                        onClick = { /* Handle video click */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Watch on YouTube")
                    }
                }
            }
        }
    }
}