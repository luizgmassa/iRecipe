package com.massa.irecipe.di

import com.massa.irecipe.data.api.RecipeApiService
import com.massa.irecipe.data.api.RetrofitClient
import org.koin.dsl.module

val networkModule = module {
    single<RecipeApiService> {
        RetrofitClient.instance
    }
}

val appModules = listOf(
    networkModule
)