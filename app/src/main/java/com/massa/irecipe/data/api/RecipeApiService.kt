package com.massa.irecipe.data.api

import com.massa.irecipe.data.model.remote.RecipeApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {
    @GET("receitas/todas")
    suspend fun getAllRecipes(): List<RecipeApiResponse>

    @GET("receitas/{id}")
    suspend fun getRecipeDetails(@Path("id") recipeId: Int): RecipeApiResponse
}