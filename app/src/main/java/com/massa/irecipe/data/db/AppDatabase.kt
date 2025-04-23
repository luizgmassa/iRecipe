package com.massa.irecipe.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.massa.irecipe.data.model.local.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recipeDao(): RecipeDao
}
