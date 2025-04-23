package com.massa.irecipe.data.model.remote

data class RecipeApiResponse(
    val id: String,
    val title: String,
    val ingredients: List<String>,
    val instructions: String
)