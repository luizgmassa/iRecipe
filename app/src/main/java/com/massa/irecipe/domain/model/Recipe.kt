package com.massa.irecipe.domain.model

data class Recipe(
    val id: String,
    val title: String,
    val ingredients: List<String>,
    val instructions: String
)