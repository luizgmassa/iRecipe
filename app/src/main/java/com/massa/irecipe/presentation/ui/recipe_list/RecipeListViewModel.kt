package com.massa.irecipe.presentation.ui.recipe_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massa.irecipe.R
import com.massa.irecipe.domain.model.Recipe
import com.massa.irecipe.domain.model.ResultWrapper
import com.massa.irecipe.domain.usecases.GetRecipesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeListViewModel(
    private val getRecipesUseCase: GetRecipesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeListUiState>(RecipeListUiState.Loading)
    val uiState: StateFlow<RecipeListUiState> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var allRecipes = emptyList<Recipe>()

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = RecipeListUiState.Loading
            val result = getRecipesUseCase()

            when (result) {
                is ResultWrapper.Success -> {
                    if (result.value.isNotEmpty()) {
                        allRecipes = result.value
                        filterRecipes()
                    } else {
                        _uiState.value = RecipeListUiState.Empty
                    }
                }

                is ResultWrapper.NetworkError -> {
                    _uiState.value = RecipeListUiState.Error(
                        messageId = R.string.error_loading_recipes
                    )
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        filterRecipes()
    }

    private fun filterRecipes() {
        val query = _searchQuery.value
        var filtered = allRecipes

        if (query.isNotBlank()) {
            val searchTerms = query.split(" ").filter { it.isNotBlank() }
            filtered = allRecipes.filter { recipe ->
                searchTerms.all { term ->
                    recipe.title.contains(term, ignoreCase = true) ||
                            recipe.ingredients.any { it.contains(term, ignoreCase = true) } ||
                            recipe.baseIngredients.any { it.contains(term, ignoreCase = true) }
                }
            }
        }

        _uiState.value = RecipeListUiState.Success(filtered)
    }
}

sealed interface RecipeListUiState {
    data object Loading : RecipeListUiState
    data object Empty : RecipeListUiState
    data class Error(val messageId: Int) : RecipeListUiState
    data class Success(val recipes: List<Recipe>) : RecipeListUiState
}