package com.example.mad_lab2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mad_lab2.data.model.CategoryModel
import com.example.mad_lab2.data.model.MealsModel
import com.example.mad_lab2.data.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _meals = MutableStateFlow<List<MealsModel>>(emptyList())
    val meals: StateFlow<List<MealsModel>> = _meals.asStateFlow()

    private val _categories = MutableStateFlow<List<CategoryModel>>(emptyList())
    val categories: StateFlow<List<CategoryModel>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchMeals()
        fetchCategories()
    }

    private fun fetchMeals() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val allMeals = mutableListOf<MealsModel>()

                // Fetch multiple pages of meals
                for (page in 1..3) {
                    val result = repository.fetchMeals(page = page)
                    allMeals.addAll(result)
                }

                _meals.value = allMeals
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val result = repository.fetchCategories()
                _categories.value = result
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}