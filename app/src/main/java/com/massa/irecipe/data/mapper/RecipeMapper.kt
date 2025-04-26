package com.massa.irecipe.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.massa.irecipe.data.model.local.RecipeEntity
import com.massa.irecipe.data.model.remote.RecipeApiResponse
import com.massa.irecipe.domain.model.Recipe

fun RecipeApiResponse.mapToEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        title = this.title,
        ingredients = this.ingredients,
        instructions = this.instructions,
        imageUrl = this.imageUrl,
        type = this.type,
        createdAt = this.createdAt,
        baseIngredients = Gson().toJson(
            this.baseIngredients.flatMap { it.ingredientNames }
        )
    )
}

fun RecipeEntity.mapToDomain(): Recipe {
    return Recipe(
        id = this.id,
        title = this.title,
        ingredients = this.ingredients.split(",").map { it.trim() },
        instructions = this.instructions,
        imageUrl = this.imageUrl,
        type = this.type,
        createdAt = this.createdAt,
        baseIngredients = Gson().fromJson(
            this.baseIngredients,
            object : TypeToken<List<String>>() {}.type
        )
    )
}

