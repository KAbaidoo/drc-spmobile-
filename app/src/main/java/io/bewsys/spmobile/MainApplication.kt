package io.bewsys.spmobile

import android.app.Application
import io.bewsys.spmobile.di.viewModelKoinModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidContext(this@MainApplication)
            modules(viewModelKoinModule)
        }
    }
}