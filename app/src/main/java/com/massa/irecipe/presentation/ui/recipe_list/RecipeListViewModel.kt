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

    init {
        loadRecipes()
    }

    fun loadRecipes() {
        viewModelScope.launch {
            _uiState.value = RecipeListUiState.Loading
            val result = getRecipesUseCase()

            when (result) {
                is ResultWrapper.Success -> {
                    _uiState.value = if (result.value.isNotEmpty()) {
                        RecipeListUiState.Success(result.value)
                    } else {
                        RecipeListUiState.Empty
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
}

sealed interface RecipeListUiState {
    data object Loading : RecipeListUiState
    data object Empty : RecipeListUiState
    data class Error(val messageId: Int) : RecipeListUiState
    data class Success(val recipes: List<Recipe>) : RecipeListUiState
}