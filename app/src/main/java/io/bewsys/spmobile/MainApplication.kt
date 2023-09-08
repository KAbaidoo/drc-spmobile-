package io.bewsys.spmobile

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics

import io.bewsys.spmobile.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG)

        startKoin{
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

}