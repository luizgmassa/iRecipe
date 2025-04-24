package com.massa.irecipe.di

import androidx.room.Room
import com.massa.irecipe.data.api.RecipeApiService
import com.massa.irecipe.data.api.RetrofitClient
import com.massa.irecipe.data.datasource.local.LocalDataSource
import com.massa.irecipe.data.datasource.remote.RemoteDataSource
import com.massa.irecipe.data.db.AppDatabase
import com.massa.irecipe.data.repository.RecipeRepositoryImpl
import com.massa.irecipe.domain.repository.RecipeRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {
    single<RecipeApiService> {
        RetrofitClient.instance
    }
}

val databaseModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "iRecipe-db"
        ).build()
    }

    single { get<AppDatabase>().recipeDao() }
}

val dataSourceModule = module {
    single<RemoteDataSource> { RemoteDataSource(get()) }
    single<LocalDataSource> { LocalDataSource(get()) }
}

val repositoryModule = module {
    single<RecipeRepository> { RecipeRepositoryImpl(get(), get()) }
}

val appModules = listOf(
    networkModule,
    databaseModule,
    dataSourceModule,
    repositoryModule
)