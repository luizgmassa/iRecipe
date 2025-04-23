package com.massa.irecipe

import android.app.Application
import com.massa.irecipe.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class RecipeApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@RecipeApplication)
            modules(appModules)
        }
    }
}