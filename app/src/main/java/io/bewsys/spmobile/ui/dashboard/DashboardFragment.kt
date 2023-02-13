package io.bewsys.spmobile.ui.dashboard

import android.os.Bundle

import android.view.View
import androidx.fragment.app.Fragment


import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentDashboardBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

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

    }

}