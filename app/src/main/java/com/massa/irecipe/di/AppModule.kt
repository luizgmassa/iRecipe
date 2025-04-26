package com.massa.irecipe.di

import androidx.room.Room
import com.massa.irecipe.data.api.RecipeApiService
import com.massa.irecipe.data.api.RetrofitClient
import com.massa.irecipe.data.datasource.local.LocalDataSource
import com.massa.irecipe.data.datasource.remote.RemoteDataSource
import com.massa.irecipe.data.db.AppDatabase
import com.massa.irecipe.data.repository.RecipeRepositoryImpl
import com.massa.irecipe.data.security.EncryptionKeyManager
import com.massa.irecipe.domain.repository.RecipeRepository
import com.massa.irecipe.domain.usecases.GetRecipeDetailsUseCase
import com.massa.irecipe.domain.usecases.GetRecipesUseCase
import com.massa.irecipe.presentation.ui.recipe_details.RecipeDetailsViewModel
import com.massa.irecipe.presentation.ui.recipe_list.RecipeListViewModel
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single<RecipeApiService> {
        RetrofitClient.instance
    }
}

val viewModelsModule = module {
    viewModel {
        RecipeListViewModel(get())
    }

    viewModel { RecipeDetailsViewModel(get()) }
}

val useCasesModule = module {
    factory {
        GetRecipesUseCase(get())
    }

    factory { GetRecipeDetailsUseCase(get()) }
}

val databaseModule = module {
    single { EncryptionKeyManager(androidContext()) }

    single {
        // TODO add:
        //  - DB auto migrations
        //  - DB manual migrations
        //  - DB migration from non-encrypted to encrypted DB

        val keyManager: EncryptionKeyManager = get()
        val passphrase = SQLiteDatabase.getBytes(keyManager.getOrCreateKey())
        val factory = SupportFactory(passphrase)

        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "iRecipe-db"
        )
            .openHelperFactory(factory)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()
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
    repositoryModule,
    viewModelsModule,
    useCasesModule
)