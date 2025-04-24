package com.massa.irecipe.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val ingredients: String,
    val instructions: String,
    val imageUrl: String,
    val type: String,
    val createdAt: String,
    val baseIngredients: String
)