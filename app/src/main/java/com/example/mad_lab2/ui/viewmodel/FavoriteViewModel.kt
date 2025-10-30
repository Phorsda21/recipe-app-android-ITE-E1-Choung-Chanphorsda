package com.example.mad_lab2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad_lab2.data.model.FavoriteEntity
import com.example.mad_lab2.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteState(
    val favoriteList: List<FavoriteEntity> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteState())
    val uiState: StateFlow<FavoriteState> = _uiState.asStateFlow()

    init {
        fetchLocalFavoriteList()
    }

    private fun fetchLocalFavoriteList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                repository.getAllFavorites().collect { result ->
                    _uiState.value = _uiState.value.copy(
                        favoriteList = result,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun removeFavorite(favorite: FavoriteEntity) {
        viewModelScope.launch {
            repository.removeFavorite(favorite)
        }
    }
}