package io.bewsys.spmobile.ui.dashboard

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.COMMUNITIES
import io.bewsys.spmobile.GROUPMENTS
import io.bewsys.spmobile.HOUSEHOLDS
import io.bewsys.spmobile.MEMEBERS
import io.bewsys.spmobile.PROVINCES
import io.bewsys.spmobile.R
import io.bewsys.spmobile.TERRITORIES
import io.bewsys.spmobile.databinding.FragmentDashboardBinding
import io.bewsys.spmobile.ui.common.BaseFragment
import io.bewsys.spmobile.ui.common.BaseViewModel
import io.bewsys.spmobile.util.exhaustive
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "DashboardFragment"

class DashboardFragment :
    BaseFragment<FragmentDashboardBinding>(FragmentDashboardBinding::inflate){
    val viewModel: DashboardViewModel by viewModel()
    override fun FragmentDashboardBinding.initialize() {





        viewModel.provinceAndCommunity.observe(viewLifecycleOwner) {
            textViewProvinces.text = it.first.toString()
            textViewCommunities.text = it.second.toString()
        }
        viewModel.territoryAndGroupement.observe(viewLifecycleOwner) {
            textViewTerritories.text = it.first.toString()
            textViewGroupements.text = it.second.toString()
        }

        viewModel.householdAndMembers.observe(viewLifecycleOwner) {
            textViewHouseholds.text = it.first.toString()
            tvMembers.text = it.second.toString()
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collect { event ->
                when (event) {
                    is BaseViewModel.Event.ShowSnackBar -> {
                        showSnackBar(event.msg)
                    }

                    is BaseViewModel.Event.Loading ->
                        showProgressBar(true)


                    is BaseViewModel.Event.Error -> {
                        showProgressBar(false)
                        showSnackBar(event.errorMsg)
                    }

                    is BaseViewModel.Event.Successful -> {
                        showProgressBar(false)
                        viewModel.showDashboardUpdatedSuccessfulMessage()
                    }

                }.exhaustive
            }
        }


        setFragmentResultListener("user_request") { _, bundle ->
            val result = bundle.getInt("user_result")
            viewModel.onResult(result)
        }

//
        communitiesCard?.setOnClickListener {
            navigateTo(R.id.communities_detail_fragment)
        }
        provincesCard?.setOnClickListener {
            navigateTo(R.id.provinces_detail_fragment)
        }

        groupementsCard?.setOnClickListener {
            navigateTo(R.id.groupments_detail_fragment)
        }
        territoriesCard?.setOnClickListener {
            navigateTo(R.id.territories_detail_fragment)
        }

        householdsCard?.setOnClickListener {
            navigateTo(R.id.nav_household)
        }
        membersCard?.setOnClickListener {
            navigateTo(R.id.membersDetailFragment)
        }




    }



}