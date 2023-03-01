package io.bewsys.spmobile.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.api.KtorHttpClient
import io.bewsys.spmobile.api.UserApi
import io.bewsys.spmobile.data.repository.*
import io.bewsys.spmobile.prefsstore.PreferencesManager
import io.bewsys.spmobile.ui.dashboard.DashboardViewModel
import io.bewsys.spmobile.ui.households.HouseholdsViewModel
import io.bewsys.spmobile.ui.households.forms.SharedDevelopmentalFormViewModel
import io.bewsys.spmobile.ui.login.LoginViewModel
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingViewModel
import io.bewsys.spmobile.ui.nonconsenting.form.AddNonConsentingHouseholdViewModel
import io.bewsys.spmobile.ui.profile.ProfileViewModel
import io.bewsys.spmobile.ui.targeting.TargetingViewModel
import io.bewsys.spmobile.work.UploadWorker
import io.ktor.client.*
import io.ktor.client.engine.android.*
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module {

    single<SqlDriver> { AndroidSqliteDriver(Database.Schema, androidContext(), "sp.db") }
    single { Database(get()) }
    single { KtorHttpClient(HttpClient(Android).engine).getClient() }
    single { PreferencesManager(androidContext()) }


    factory { UserApi(get()) }
    factory { CommunityRepositoryImpl(get()) }
    factory { ProvinceRepositoryImpl(get()) }
    factory { HouseholdRepositoryImpl(get()) }
    factory { NonConsentingHouseholdRepositoryImpl(get()) }
    factory { UserRepositoryImpl(get()) }


    worker { UploadWorker(androidContext(), get()) }

    viewModel { DashboardViewModel(get(), get(), get()) }
    viewModel { HouseholdsViewModel(get(), get()) }
    viewModel { NonConsentingViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { TargetingViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { AddNonConsentingHouseholdViewModel(get(), get(), get(), get(), get()) }
    viewModel { SharedDevelopmentalFormViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }


}