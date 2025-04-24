package com.massa.irecipe.data.model.remote

import com.google.gson.annotations.SerializedName

data class BaseIngredientResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nomesIngrediente")
    val ingredientNames: List<String>,

    @SerializedName("receita_id")
    val recipeId: Int,

    @SerializedName("created_at")
    val createdAt: String
)
