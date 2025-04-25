package com.massa.irecipe.domain.usecases

import com.massa.irecipe.domain.repository.RecipeRepository

class GetRecipesUseCase(private val repository: RecipeRepository) {
    suspend operator fun invoke() = repository.getRecipes()
}