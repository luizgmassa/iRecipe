package com.massa.irecipe.domain.model

data class Recipe(
    val id: Int,
    val title: String,
    val ingredients: List<String>,
    val instructions: String,
    val imageUrl: String,
    val type: String,
    val createdAt: String,
    val baseIngredients: List<String>
)