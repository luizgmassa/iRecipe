package com.massa.irecipe.data.datasource.local

import com.massa.irecipe.data.db.RecipeDao
import com.massa.irecipe.data.model.local.RecipeEntity

class LocalDataSource(private val recipeDao: RecipeDao) {
    suspend fun getRecipes(): List<RecipeEntity> = recipeDao.getAllRecipes()

    suspend fun saveRecipes(recipes: List<RecipeEntity>) = recipeDao.insertAll(recipes)
}