package com.massa.irecipe.data.repository

import android.util.Log
import com.massa.irecipe.data.datasource.local.LocalDataSource
import com.massa.irecipe.data.datasource.remote.RemoteDataSource
import com.massa.irecipe.data.mapper.mapToDomain
import com.massa.irecipe.data.mapper.mapToEntity
import com.massa.irecipe.data.model.local.RecipeEntity
import com.massa.irecipe.domain.model.Recipe
import com.massa.irecipe.domain.model.ResultWrapper
import com.massa.irecipe.domain.repository.RecipeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecipeRepository {
    override suspend fun getRecipes(): ResultWrapper<List<Recipe>> {
        return ResultWrapper.Success(
            withContext(dispatcher) {
                val localRecipes = localDataSource.getRecipes()
                if (localRecipes.isEmpty()) {
                    refreshRecipes()
                    localDataSource.getRecipes().map { it.mapToDomain() }
                } else {
                    localRecipes.map { it.mapToDomain() }
                }
            }
        )
    }

    override suspend fun refreshRecipes(): ResultWrapper<List<RecipeEntity>> {
        return withContext(dispatcher) {
            try {
                val remoteRecipes = remoteDataSource.getRecipes()
                localDataSource.clearRecipes()

                val recipes = remoteRecipes.map { it.mapToEntity() }
                localDataSource.saveRecipes(recipes)

                ResultWrapper.Success(recipes)
            } catch (e: Exception) {
                // TODO encapsulate logging error
                Log.e(this.javaClass.name, "Error getting recipes", e)
                ResultWrapper.NetworkError
            }
        }
    }

    override suspend fun getRecipeDetails(recipeId: Int): ResultWrapper<Recipe> {
        return withContext(dispatcher) {
            try {
                val localRecipe = localDataSource.getRecipeById(recipeId)?.mapToDomain()

                localRecipe?.let {
                    ResultWrapper.Success(it)
                } ?: run {
                    val remoteRecipe = remoteDataSource.getRecipeDetails(recipeId)
                    val entity = remoteRecipe.mapToEntity()
                    localDataSource.saveRecipes(listOf(entity))
                    ResultWrapper.Success(entity.mapToDomain())
                }
            } catch (e: Exception) {
                // TODO encapsulate logging error
                Log.e(this.javaClass.name, "Error getting one recipe", e)
                ResultWrapper.NetworkError
            }
        }
    }
}