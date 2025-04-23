package com.massa.irecipe.data.datasource.remote

import com.massa.irecipe.data.api.RecipeApiService
import com.massa.irecipe.data.model.remote.RecipeApiResponse

class RemoteDataSource(private val apiService: RecipeApiService) {
    suspend fun getRecipes(): List<RecipeApiResponse> = apiService.getAllRecipes()
}