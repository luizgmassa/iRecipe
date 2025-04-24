package com.massa.irecipe.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.massa.irecipe.data.model.local.RecipeEntity
import com.massa.irecipe.data.model.remote.RecipeApiResponse
import com.massa.irecipe.domain.model.Recipe

class RecipeMapper {
    fun mapToEntity(apiResponse: RecipeApiResponse): RecipeEntity {
        return RecipeEntity(
            id = apiResponse.id,
            title = apiResponse.title,
            ingredients = apiResponse.ingredients,
            instructions = apiResponse.instructions,
            imageUrl = apiResponse.imageUrl,
            type = apiResponse.type,
            createdAt = apiResponse.createdAt,
            baseIngredients = Gson().toJson(
                apiResponse.baseIngredients.flatMap { it.ingredientNames }
            )
        )
    }

    fun mapToDomain(entity: RecipeEntity): Recipe {
        return Recipe(
            id = entity.id,
            title = entity.title,
            ingredients = entity.ingredients.split(",").map { it.trim() },
            instructions = entity.instructions,
            imageUrl = entity.imageUrl,
            type = entity.type,
            createdAt = entity.createdAt,
            baseIngredients = Gson().fromJson(
                entity.baseIngredients,
                object : TypeToken<List<String>>() {}.type
            )
        )
    }
}
