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
import io.bewsys.spmobile.ui.households.HouseholdsFragment
import io.bewsys.spmobile.ui.households.HouseholdsFragmentDirections
import io.bewsys.spmobile.util.exhaustive
import org.koin.androidx.viewmodel.ext.android.viewModel


class NonConsentingFragment : Fragment(R.layout.fragment_non_consenting),
    NonConsentingHouseholdAdapter.OnItemClickListener {
    val viewModel: NonConsentingViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


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
                            findNavController().navigate(action)

                        }

                        is NonConsentingViewModel.NonConsentingEvent.ShowNonConsentingHouseholdSavedConfirmationMessage -> Snackbar.make(
                            requireView(),
                            event.msg,
                            Snackbar.LENGTH_SHORT
                        ).show()

                        is NonConsentingViewModel.NonConsentingEvent.NavigateToEditNonConsentingHouseholdsForm -> {
                            val action =
                                NonConsentingFragmentDirections.actionNavNonConsentingToNonConsentingFormFragment(
                                    title = getString(R.string.edit_non_consenting_household),
                                    household = event.nonConsentingHousehold
                                )
                            findNavController().navigate(action)
                        }

                        is NonConsentingViewModel.NonConsentingEvent.Exception -> {
                            progressBar.isVisible = false
                            val action =
                                HouseholdsFragmentDirections.actionGlobalLoginDialogFragment(event.errMsg)
                            findNavController().navigate(action)
                        }

                        is NonConsentingViewModel.NonConsentingEvent.Failure -> {
                            progressBar.isVisible = false
                            val action =
                                HouseholdsFragmentDirections.actionGlobalLoginDialogFragment(event.errMsg)
                            findNavController().navigate(action)
                        }

                        is NonConsentingViewModel.NonConsentingEvent.Loading -> progressBar.isVisible =
                            true

                        is NonConsentingViewModel.NonConsentingEvent.Successful -> {
                            progressBar.isVisible = false

//                        val msg = getString(
//                            R.string.upload_success_message,
//                            "${event.households}",
//                            "${event.members}"
//                        )
                            val action =
                                HouseholdsFragmentDirections.actionGlobalLoginDialogFragment(event.msg)
                            findNavController().navigate(action)
                        }


                    }
                }.exhaustive
            }
        }
//         set up menu
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
