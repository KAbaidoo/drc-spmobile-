package io.bewsys.spmobile.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.repository.*
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.*
import io.bewsys.spmobile.ui.dashboard.DashboardViewModel
import io.bewsys.spmobile.ui.households.HouseholdsViewModel
import io.bewsys.spmobile.ui.households.forms.developmentalform.SharedDevelopmentalFormViewModel
import io.bewsys.spmobile.ui.auth.LoginViewModel
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingViewModel
import io.bewsys.spmobile.ui.nonconsenting.form.AddNonConsentingHouseholdViewModel
import io.bewsys.spmobile.ui.profile.ProfileViewModel
import io.bewsys.spmobile.ui.targeting.TargetingViewModel
import io.bewsys.spmobile.ui.MainViewModel
import io.bewsys.spmobile.ui.auth.ForgotPasswordViewModel
import io.bewsys.spmobile.ui.auth.LoginDialogViewModel
import io.bewsys.spmobile.ui.households.delete.DeleteHouseholdViewModel
import io.bewsys.spmobile.ui.households.detail.HouseholdDetailViewModel
import io.bewsys.spmobile.util.LocationProvider
import io.bewsys.spmobile.util.provideApplicationScope
import io.bewsys.spmobile.work.HouseholdUploadWorker
import io.bewsys.spmobile.work.NonConsentUploadWorker

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val appModule = module {

    single<SqlDriver> { AndroidSqliteDriver(Database.Schema, androidContext(), "sp.db") }
    single { Database(get()) }
    single { KtorHttpClient(androidContext()).getClient() }
    single { PreferencesManager(androidContext()) }
    single { provideApplicationScope() }
    single { LocationProvider(androidContext()) }

    factory { AuthApi(get()) }
    factory { HouseholdApi(get()) }
    factory { NonConsentingHouseholdApi(get()) }
    factory { DashboardApi(get()) }

    factory { DashboardRepository(get(),get(),get(),get()) }
    factory { HouseholdRepository(get(),get(),get(),get()) }
    factory { NonConsentingHouseholdRepository(get(),get(),get()) }
    factory { AuthRepository(get(),get()) }

    worker { NonConsentUploadWorker(androidContext(), get()) }
    worker { HouseholdUploadWorker(androidContext(), get()) }

    viewModel { DashboardViewModel(get(),get()) }
    viewModel { HouseholdsViewModel(get(), get(),get()) }
    viewModel { NonConsentingViewModel(get(), get()) }
    viewModel { ProfileViewModel(get(),get()) }
    viewModel { TargetingViewModel(get()) }
    viewModel { AddNonConsentingHouseholdViewModel(get(), get(), get(), get())}
    viewModel { SharedDevelopmentalFormViewModel(get(), get(),get(),get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel(get()) }
    viewModel { LoginDialogViewModel(get()) }
    viewModel { DeleteHouseholdViewModel(get(),get(), get()) }
    viewModel { ForgotPasswordViewModel(get(),get()) }
    viewModel{HouseholdDetailViewModel(get())}
}
//HttpClient(Android).engine