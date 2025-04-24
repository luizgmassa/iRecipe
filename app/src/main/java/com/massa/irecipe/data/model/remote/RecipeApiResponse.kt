package com.massa.irecipe.data.model.remote

import com.google.gson.annotations.SerializedName

data class RecipeApiResponse(
    @SerializedName("id")
    val id: Int,

    @SerializedName("receita")
    val title: String,

    @SerializedName("ingredientes")
    val ingredients: String,

    @SerializedName("modo_preparo")
    val instructions: String,

    @SerializedName("link_imagem")
    val imageUrl: String,

    @SerializedName("tipo")
    val type: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("IngredientesBase")
    val baseIngredients: List<BaseIngredientResponse>
)
