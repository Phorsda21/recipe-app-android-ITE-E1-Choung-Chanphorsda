package com.example.mad_lab2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.mad_lab2.data.model.FavoriteEntity
import com.example.mad_lab2.ui.viewmodel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    onMealClick: (String) -> Unit,
    viewModel: FavoriteViewModel
) {
    val viewModel: FavoriteViewModel = hiltViewModel()
    val viewState = viewModel.uiState.collectAsState()
    val state = viewState.value





    if (state.favoriteList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text("❤️", fontSize = 64.sp)
                Text(
                    "No favorites yet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text("Save your favorite recipes to see them here")
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {

            // App Title
            Text(
                text = "Recipe Finder",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Text(
                text = "Favorite Recipes",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(state.favoriteList, key = { it.id }) { favorite ->
                    FavoriteGridCard(
                        favorite = favorite,
                        onMealClick = { onMealClick(favorite.id) },
                        onDelete = { viewModel.removeFavorite(favorite) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteGridCard(
    favorite: FavoriteEntity,
    onMealClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onMealClick() },
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = favorite.mealThumb,
                    contentDescription = favorite.meal,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            RoundedCornerShape(12.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = favorite.meal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        maxLines = 2
                    )
                    Text(
                        text = favorite.category ?: "Unknown",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        RoundedCornerShape(8.dp)
                    )
            ) {
                Icon(
                    Icons.Filled.Delete,
                    "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}