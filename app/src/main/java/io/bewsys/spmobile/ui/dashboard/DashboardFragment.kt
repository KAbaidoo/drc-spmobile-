package io.bewsys.spmobile.ui.dashboard

import android.os.Bundle
import android.util.Log

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
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


        setFragmentResultListener("user_request") { _, bundle ->
            val result = bundle.getInt("user_result")
            viewModel.onResult(result)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dashboardEvent.collect { event ->
                when (event) {
                    is DashboardViewModel.DashboardEvent.ShowLoginSuccessfulMessage -> {
                        Snackbar.make(
                            requireView(),
                            event.msg,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    is DashboardViewModel.DashboardEvent.ShowUpdateSuccessfulMessage -> {

                        Snackbar.make(
                            requireView(),
                            event.msg,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }


    }

}