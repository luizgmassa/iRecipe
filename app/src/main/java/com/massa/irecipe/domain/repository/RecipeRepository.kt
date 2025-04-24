package com.massa.irecipe.domain.repository

import com.massa.irecipe.data.model.local.RecipeEntity
import com.massa.irecipe.domain.model.Recipe
import com.massa.irecipe.domain.model.ResultWrapper

interface RecipeRepository {
    suspend fun getRecipes(): ResultWrapper<List<Recipe>>
    suspend fun refreshRecipes(): ResultWrapper<List<RecipeEntity>>
}