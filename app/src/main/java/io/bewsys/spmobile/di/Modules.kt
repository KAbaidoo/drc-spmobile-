package io.bewsys.spmobile.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.api.Api
import io.bewsys.spmobile.data.repository.CommunityRepositoryImpl
import io.bewsys.spmobile.data.repository.HouseholdRepositoryImpl
import io.bewsys.spmobile.data.repository.NonConsentingHouseholdRepositoryImpl
import io.bewsys.spmobile.data.repository.ProvinceRepositoryImpl
import io.bewsys.spmobile.ui.dashboard.DashboardViewModel
import io.bewsys.spmobile.ui.households.HouseholdsViewModel
import io.bewsys.spmobile.ui.households.forms.SharedDevelopmentalFormViewModel
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

    single { Api(HttpClient(Android).engine).getClient()}


    factory { CommunityRepositoryImpl(get()) }
    factory { ProvinceRepositoryImpl(get()) }
    factory { HouseholdRepositoryImpl(get()) }
    factory { NonConsentingHouseholdRepositoryImpl(get()) }

    worker { UploadWorker( androidContext(), get()) }

    viewModel { DashboardViewModel(get(), get()) }
    viewModel { HouseholdsViewModel(get(), get()) }
    viewModel { NonConsentingViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { TargetingViewModel() }
    viewModel { ProfileViewModel(get()) }
    viewModel { AddNonConsentingHouseholdViewModel(get(), get(), get(), get(), get()) }
    viewModel { SharedDevelopmentalFormViewModel(get(), get()) }


}