package com.example.comics

import androidx.multidex.MultiDexApplication
import com.example.comics.di.DI
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class Application : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@Application)
            modules(DI.modules)
        }
    }
}