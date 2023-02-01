package io.bewsys.spmobile.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.ui.dashboard.DashboardViewModel
import io.bewsys.spmobile.ui.households.HouseholdsViewModel
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingViewModel
import io.bewsys.spmobile.ui.profile.ProfileViewModel
import io.bewsys.spmobile.ui.targeting.TargetingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<SqlDriver> { AndroidSqliteDriver(Database.Schema,androidContext(),"sp.db") }
    single { Database(get()) }

    viewModel { DashboardViewModel(get()) }
    viewModel { HouseholdsViewModel(get()) }
    viewModel { NonConsentingViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { TargetingViewModel(get()) }
    viewModel { ProfileViewModel(get()) }


}