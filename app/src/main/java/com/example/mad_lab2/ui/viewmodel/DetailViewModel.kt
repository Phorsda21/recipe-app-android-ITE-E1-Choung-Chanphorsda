package com.example.mad_lab2.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.mad_lab2.data.model.FavoriteEntity
import com.example.mad_lab2.data.model.MealsModel
import com.example.mad_lab2.data.repository.RecipeRepository
import com.example.mad_lab2.ui.navigation.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailState(
    val loading: Boolean = false,
    val meal: MealsModel = MealsModel(),
    val isFavorite: Boolean = false
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: RecipeRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailState())
    val uiState: StateFlow<DetailState> = _uiState.asStateFlow()

    private val mealId = savedStateHandle.get<String>("mealId")

    init {
        mealId?.let {
            fetchMealDetails(it)
            checkIfFavorite(it)
        }
    }

    private fun fetchMealDetails(id: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                val meals = repository.fetchMeals()
                val meal = meals.find { it.id == id }
                meal?.let {
                    _uiState.value = _uiState.value.copy(
                        meal = meal,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(loading = false)
            }
        }
    }

    fun checkIfFavorite(id: String) {
        viewModelScope.launch {
            val isFav = repository.isFavorite(id)
            _uiState.value = _uiState.value.copy(isFavorite = isFav)
        }
    }

    fun addToLocalFavorite() {
        viewModelScope.launch {
            val favorite = _uiState.value.meal.toFavoriteEntity()
            repository.addFavorite(favorite)
            _uiState.value = _uiState.value.copy(isFavorite = true)
        }
    }

    fun removeLocalFavorite(id: String) {
        viewModelScope.launch {
            val favorite = _uiState.value.meal.toFavoriteEntity()
            repository.removeFavorite(favorite)
            _uiState.value = _uiState.value.copy(isFavorite = false)
        }
    }

    fun toggleFavorite() {
        if (_uiState.value.isFavorite) {
            removeLocalFavorite(_uiState.value.meal.id ?: "")
        } else {
            addToLocalFavorite()
        }
    }
}

private fun MealsModel.toFavoriteEntity() = FavoriteEntity(
    id = this.id ?: "0",
    meal = this.meal ?: "NA",
    mealThumb = this.mealThumb ?: "NA",
    category = this.category,
    area = this.area,
    instructions = this.instructions,
    ingredients = this.ingredients?.joinToString { "${it.ingredient}: ${it.measure}" }
)