package com.massa.irecipe.presentation.ui.recipe_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.massa.irecipe.R
import com.massa.irecipe.domain.model.Recipe
import com.massa.irecipe.domain.model.ResultWrapper
import com.massa.irecipe.domain.usecases.GetRecipeDetailsUseCase
import com.massa.irecipe.presentation.ui.recipe_details.RecipeDetailsUiState.Loading
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecipeDetailsViewModel(
    private val getRecipeDetailsUseCase: GetRecipeDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<RecipeDetailsUiState>(Loading)
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState

    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.value = Loading

            val result = getRecipeDetailsUseCase(recipeId)

            when (result) {
                is ResultWrapper.Success -> {
                    _uiState.value = RecipeDetailsUiState.Success(result.value)
                }

                is ResultWrapper.NetworkError -> {
                    _uiState.value = RecipeDetailsUiState.Error(
                        messageId = R.string.error_loading_recipe
                    )
                }
            }
        }
    }
}

sealed class RecipeDetailsUiState {
    data class Success(val recipe: Recipe) : RecipeDetailsUiState()
    data class Error(val messageId: Int) : RecipeDetailsUiState()
    data object Loading : RecipeDetailsUiState()
}
