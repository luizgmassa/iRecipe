package com.massa.irecipe.data.api

import com.massa.irecipe.data.model.remote.RecipeApiResponse
import retrofit2.http.GET

interface RecipeApiService {
    @GET("receitas/todas")
    suspend fun getAllRecipes(): List<RecipeApiResponse>
}