package io.bewsys.spmobile.ui.dashboard

import android.os.Bundle

import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentDashboardBinding
import io.bewsys.spmobile.util.exhaustive
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "DashboardFragment"

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: DashboardViewModel by viewModel()
        val binding = FragmentDashboardBinding.bind(view)

        binding.apply {
            viewModel.provinceAndCommunity.observe(viewLifecycleOwner) {
                textViewProvinces.text = it.first.toString()
                textViewCommunities.text = it.second.toString()
            }
            viewModel.territoryAndGroupement.observe(viewLifecycleOwner) {
                textViewTerritories.text = it.first.toString()
                textViewGroupements.text = it.second.toString()
            }

            viewModel.householdAndMembers.observe(viewLifecycleOwner) {
                textViewHouseholds.text   = it.first.toString()
                tvMembers.text = it.second.toString()
            }




            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.dashboardEvent.collect { event ->
                    when (event) {
                        is DashboardViewModel.DashboardEvent.ShowMessage -> {
                            Snackbar.make(
                                requireView(),
                                event.msg,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        is DashboardViewModel.DashboardEvent.Loading -> progressBar.isVisible = true

                        is DashboardViewModel.DashboardEvent.Exception, DashboardViewModel.DashboardEvent.Failure-> {
                            progressBar.isVisible = false
                        }
                        is  DashboardViewModel.DashboardEvent.Successful -> {
                            progressBar.isVisible = false
                            viewModel.showDashboardUpdatedSuccessfulMessage()
                        }

                    }.exhaustive
                }
            }
        }

        setFragmentResultListener("user_request") { _, bundle ->
            val result = bundle.getInt("user_result")
            viewModel.onResult(result)
        }


    }

}