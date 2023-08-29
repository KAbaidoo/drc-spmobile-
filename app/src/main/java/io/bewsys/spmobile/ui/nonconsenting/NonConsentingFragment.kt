package io.bewsys.spmobile.ui.nonconsenting

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.R
import io.bewsys.spmobile.data.local.NonConsentHouseholdModel
import io.bewsys.spmobile.databinding.FragmentNonConsentingBinding
import io.bewsys.spmobile.ui.common.BaseFragment
import io.bewsys.spmobile.ui.households.HouseholdsFragment
import io.bewsys.spmobile.ui.households.HouseholdsFragmentDirections
import io.bewsys.spmobile.util.exhaustive
import org.koin.androidx.viewmodel.ext.android.viewModel


class NonConsentingFragment :
    BaseFragment<FragmentNonConsentingBinding>(FragmentNonConsentingBinding::inflate),
    NonConsentingHouseholdAdapter.OnItemClickListener {
    val viewModel: NonConsentingViewModel by viewModel()
    override fun FragmentNonConsentingBinding.initialize() {
        fabNonConsenting.setOnClickListener {
            viewModel.onFabClicked()
        }

        val nonConsentingHouseholdAdapter = NonConsentingHouseholdAdapter(this@NonConsentingFragment)
        recyclerViewNonConsentingHouseholds.apply {
            adapter = nonConsentingHouseholdAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }

        viewModel.nonConsentingHouseholds.observe(viewLifecycleOwner) {
            nonConsentingHouseholdAdapter.submitList(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.nonConsentingEvent.collect { event ->
                when (event) {
                    is NonConsentingViewModel.NonConsentingEvent.NavigateToNonConsentingHouseholdsForm -> {

                        val action =
                            NonConsentingFragmentDirections.actionNavNonConsentingToNonConsentingFormFragment(
                                title = getString(R.string.add_non_consenting_household),
                                household = null
                            )
                       navigateTo(action)

                    }

                    is NonConsentingViewModel.NonConsentingEvent.ShowNonConsentingHouseholdSavedConfirmationMessage -> showSnackBar(event.msg)

                    is NonConsentingViewModel.NonConsentingEvent.NavigateToEditNonConsentingHouseholdsForm -> {
                        val action =
                            NonConsentingFragmentDirections.actionNavNonConsentingToNonConsentingFormFragment(
                                title = getString(R.string.edit_non_consenting_household),
                                household = event.nonConsentingHousehold
                            )
                        navigateTo(action)
                    }

                    is NonConsentingViewModel.NonConsentingEvent.Exception -> {
                       showProgressBar(false)
                        val action =
                            HouseholdsFragmentDirections.actionGlobalLoginDialogFragment(event.errMsg)
                        navigateTo(action)
                    }

                    is NonConsentingViewModel.NonConsentingEvent.Failure -> {
                       showProgressBar(false)
                        val action =
                            HouseholdsFragmentDirections.actionGlobalLoginDialogFragment(event.errMsg)
                       navigateTo(action)
                    }

                    is NonConsentingViewModel.NonConsentingEvent.Loading -> showProgressBar(true)

                    is NonConsentingViewModel.NonConsentingEvent.Successful -> {
                     showProgressBar(false)

                        val action =
                            HouseholdsFragmentDirections.actionGlobalLoginDialogFragment(event.msg)
                     navigateTo(action)
                    }


                }
            }.exhaustive
        }

        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_households_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.action_upload_households -> {
//                        Log.d(HouseholdsFragment.TAG, "Upload clicked")

                        viewModel.onUploadMenuItemClicked()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        setFragmentResultListener("add_non_consenting_household_request") { _, bundle ->
            val result = bundle.getInt("add_non_consenting_household_result")
            viewModel.onAddNonConsentingHouseholdResult(result)
        }
    }


    override fun onItemClick(nonConsentingHousehold: NonConsentHouseholdModel) {
        viewModel.onHousholdSelected(nonConsentingHousehold)
    }

}
