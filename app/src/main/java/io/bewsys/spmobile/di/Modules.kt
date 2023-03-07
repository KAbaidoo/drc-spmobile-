package io.bewsys.spmobile.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.remote.KtorHttpClient
import io.bewsys.spmobile.data.remote.UserApi
import io.bewsys.spmobile.data.repository.*
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.NonConsentingHouseholdApi
import io.bewsys.spmobile.ui.dashboard.DashboardViewModel
import io.bewsys.spmobile.ui.households.HouseholdsViewModel
import io.bewsys.spmobile.ui.households.forms.SharedDevelopmentalFormViewModel
import io.bewsys.spmobile.ui.login.LoginViewModel
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingViewModel
import io.bewsys.spmobile.ui.nonconsenting.form.AddNonConsentingHouseholdViewModel
import io.bewsys.spmobile.ui.profile.ProfileViewModel
import io.bewsys.spmobile.ui.targeting.TargetingViewModel
import io.bewsys.spmobile.ui.MainViewModel
import io.bewsys.spmobile.work.UploadWorker

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module {

    single<SqlDriver> { AndroidSqliteDriver(Database.Schema, androidContext(), "sp.db") }
    single { Database(get()) }
    single { KtorHttpClient().getClient() }
    single { PreferencesManager(androidContext()) }


    factory { UserApi(get()) }
    factory { NonConsentingHouseholdApi(get()) }
    factory { CommunityRepository(get()) }
    factory { ProvinceRepository(get()) }
    factory { HouseholdRepository(get()) }
    factory { NonConsentingHouseholdRepository(get(),get(),get()) }
    factory { UserRepository(get(),get()) }

    worker { UploadWorker(androidContext(), get()) }

    viewModel { DashboardViewModel(get(), get()) }
    viewModel { HouseholdsViewModel(get(), get()) }
    viewModel { NonConsentingViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get(),get()) }
    viewModel { TargetingViewModel(get()) }
    viewModel { AddNonConsentingHouseholdViewModel(get(), get(), get(), get(), get()) }
    viewModel { SharedDevelopmentalFormViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
}
//HttpClient(Android).engine