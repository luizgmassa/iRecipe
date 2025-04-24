package com.massa.irecipe.utils

import com.massa.irecipe.data.model.local.RecipeEntity
import com.massa.irecipe.data.model.remote.RecipeApiResponse
import com.massa.irecipe.domain.model.Recipe

fun List<RecipeEntity>.mapToDomain() = map {
    Recipe(
        id = it.id,
        title = it.title,
        ingredients = it.ingredients.split(","),
        instructions = it.instructions
    )
}

fun List<RecipeApiResponse>.mapToEntity() = map {
    RecipeEntity(
        id = it.id,
        title = it.title,
        ingredients = it.ingredients.joinToString(","),
        instructions = it.instructions
    )
}
