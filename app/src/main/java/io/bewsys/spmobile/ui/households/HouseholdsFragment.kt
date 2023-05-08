package io.bewsys.spmobile.ui.households

import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener

import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

import io.bewsys.spmobile.R
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.databinding.FragmentHouseholdsBinding

import io.bewsys.spmobile.util.exhaustive
import org.koin.androidx.viewmodel.ext.android.viewModel


class HouseholdsFragment : Fragment(R.layout.fragment_households),HouseholdAdapter.OnItemClickListener {
    val viewModel: HouseholdsViewModel by viewModel()
    var isOpen: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentHouseholdsBinding.bind(view)
        val householdAdapter = HouseholdAdapter(this)

        hideActions(binding)

        binding.apply {
            fabAddRegistration.setOnClickListener {
//                viewModel.onAddRegistrationFabClicked()
                viewModel.onDevelopmentalFabClicked()
            }
//            fabDevelopmentalAction.setOnClickListener {
//                viewModel.onDevelopmentalFabClicked()
//            }
//            textDevelopmentalAction.setOnClickListener {
//                viewModel.onDevelopmentalFabClicked()
//            }
//            fabHumanitarian.setOnClickListener {
//                viewModel.onHumanitarianFabClicked()
//            }
//            textHumanitarianAction.setOnClickListener {
//                viewModel.onHumanitarianFabClicked()
//            }
            recyclerViewHouseholds.apply {
                adapter = householdAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }

        viewModel.households.observe(viewLifecycleOwner){
            householdAdapter.submitList(it)
        }


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.householdsEventEvent.collect { event ->
                when (event) {
                    is HouseholdsViewModel.HouseholdEvent.AddRegistrationClicked -> {
                        if (!isOpen) showActions(binding) else hideActions(binding)
                    }
                    is HouseholdsViewModel.HouseholdEvent.DevelopmentalClicked -> {
                      val action =HouseholdsFragmentDirections.actionNavHouseholdToFormNavigation(
                          title = getString(R.string.add_household),
                          household = null
                      )
                        findNavController().navigate(action) }
                    is HouseholdsViewModel.HouseholdEvent.HumanitarianClicked -> {
                        val action = HouseholdsFragmentDirections.actionNavHouseholdToHumanitarianFormFragment()
                        findNavController().navigate(action)
                    }
                    is HouseholdsViewModel.HouseholdEvent.ShowSnackMessage -> Snackbar.make(
                        requireView(),
                        event.msg,
                        Snackbar.LENGTH_SHORT
                    ).show()
                    is HouseholdsViewModel.HouseholdEvent.NavigateToHouseholdDetailScreen -> {
                        val action = HouseholdsFragmentDirections.actionNavHouseholdToHouseholdDetailFragment(
                            event.householdModel
                        )
                        findNavController().navigate(action)

                    }
                }
            }.exhaustive
        }




        setFragmentResultListener("add_household_request") { _, bundle ->
            val result = bundle.getInt("add_household_result")
            viewModel.onHouseholdResult(result)
        }
        setFragmentResultListener("delete_household_request") { _, bundle ->
            val result = bundle.getInt("delete_household_result")
            viewModel.onHouseholdResult(result)
        }
    }

    private fun showActions(binding: FragmentHouseholdsBinding) {
        isOpen = true
        binding.fabAddRegistration.setImageResource(R.drawable.fab_close_24)
        binding.fabDevelopmentalAction.show()
        binding.fabHumanitarian.show()
        binding.textAddRegistration.visibility = View.VISIBLE
        binding.textDevelopmentalAction.visibility = View.VISIBLE
        binding.textHumanitarianAction.visibility = View.VISIBLE
    }

    private fun hideActions(binding: FragmentHouseholdsBinding) {
        isOpen = false
        binding.fabAddRegistration.setImageResource(R.drawable.menu_add_24)
        binding.fabDevelopmentalAction.hide()
        binding.fabHumanitarian.hide()
        binding.textAddRegistration.visibility = View.GONE
        binding.textDevelopmentalAction.visibility = View.GONE
        binding.textHumanitarianAction.visibility = View.GONE

    }

    override fun onItemClick(householdModel: HouseholdModel) {
        Log.d("household screen", "${householdModel.id}")
        viewModel.onHouseholdSelected(householdModel)
    }
}