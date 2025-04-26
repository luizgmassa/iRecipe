package com.massa.irecipe.domain.usecases

import com.massa.irecipe.domain.model.Recipe
import com.massa.irecipe.domain.model.ResultWrapper
import com.massa.irecipe.domain.repository.RecipeRepository

class GetRecipeDetailsUseCase(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipeId: Int): ResultWrapper<Recipe> =
        repository.getRecipeDetails(recipeId)
}