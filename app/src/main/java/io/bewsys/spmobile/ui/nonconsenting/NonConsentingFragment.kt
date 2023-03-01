package io.bewsys.spmobile.ui.nonconsenting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.R
import io.bewsys.spmobile.data.model.NonConsentHousehold
import io.bewsys.spmobile.databinding.FragmentNonConsentingBinding
import io.bewsys.spmobile.ui.dashboard.DashboardFragmentDirections
import io.bewsys.spmobile.util.exhaustive
import org.koin.androidx.viewmodel.ext.android.viewModel


class NonConsentingFragment : Fragment(R.layout.fragment_non_consenting) ,NonConsentingHouseholdAdapter.OnItemClickListener{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: NonConsentingViewModel by viewModel()
        val binding = FragmentNonConsentingBinding.bind(view)
        val nonConsentingHouseholdAdapter = NonConsentingHouseholdAdapter(this)

        binding.apply {
            fabNonConsenting.setOnClickListener {
                viewModel.onFabClicked()
            }
            recyclerViewNonConsentingHouseholds.apply {
                adapter = nonConsentingHouseholdAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.nonConsentingHouseholds.observe(viewLifecycleOwner){
            nonConsentingHouseholdAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.nonConsentingEvent.collect { event ->
                when (event) {
                    is NonConsentingViewModel.NonConsentingEvent.NavigateToNonConsentingHouseholdsForm -> {

                        val action =
                            NonConsentingFragmentDirections.actionNavNonConsentingToNonConsentingFormFragment()
                        findNavController().navigate(action)

                    }
                    is NonConsentingViewModel.NonConsentingEvent.ShowNonConsentingHouseholdSavedConfirmationMessage -> Snackbar.make(
                        requireView(),
                        event.msg,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }.exhaustive
        }

        setFragmentResultListener("add_non_consenting_household_request") { _, bundle ->
            val result = bundle.getInt("add_non_consenting_household_result")
            viewModel.onAddNonConsentingHouseholdResult(result)
        }
    }

    override fun onItemClick(nonConsentingHousehold: NonConsentHousehold) {
        TODO("Not yet implemented")
    }
}
