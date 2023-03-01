package io.bewsys.spmobile.ui.dashboard

import android.os.Bundle
import android.util.Log

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentDashboardBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "DashboardFragment"

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: DashboardViewModel by viewModel()
        val binding = FragmentDashboardBinding.bind(view)


        binding.apply {
            viewModel.counts.observe(viewLifecycleOwner) {
                textViewProvinces.text = it.first.toString()
                textViewCommunities.text = it.second.toString()
            }
        }

        viewModel.prefs.observe(viewLifecycleOwner){
            if(it.isLoggedIn.not()){
              viewModel.navigateToLoginScreen()
                Log.d(TAG, it.isLoggedIn.toString())
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dashboardEvent.collect { event ->
                when (event) {
                    is DashboardViewModel.DashboardEvent.NavigateToLogin -> {

                        findNavController().navigate(R.id.action_nav_dashboard_to_loginFragment)
                        Log.d(TAG, event.toString())
                    }
                }
            }
        }


    }

}