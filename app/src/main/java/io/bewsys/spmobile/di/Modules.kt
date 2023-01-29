package io.bewsys.spmobile.di

import io.bewsys.spmobile.ui.dashboard.DashboardViewModel
import io.bewsys.spmobile.ui.households.HouseholdsViewModel
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingViewModel
import io.bewsys.spmobile.ui.profile.ProfileViewModel
import io.bewsys.spmobile.ui.targeting.TargetingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


//val appModule: Module = module {
//
//}

val viewModelKoinModule = module {
    viewModel { DashboardViewModel(get()) }
    viewModel { HouseholdsViewModel(get()) }
    viewModel { NonConsentingViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { TargetingViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
}