package io.bewsys.spmobile.ui.households

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentHouseholdsBinding
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingFragmentDirections
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingViewModel
import io.bewsys.spmobile.util.exhaustive
import org.w3c.dom.Text

class HouseholdsFragment : Fragment(R.layout.fragment_households) {
    val viewModel: HouseholdsViewModel by viewModels()
    var isOpen: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHouseholdsBinding.bind(view)

        hideActions(binding)
        binding.fabAddRegistration.setOnClickListener {
            viewModel.onAddRegistrationFabClicked()
        }
        binding.fabDevelopmental.setOnClickListener {
            viewModel.onDevelopmentalFabClicked()
        }
        binding.textDevelopmentalAction.setOnClickListener {
            viewModel.onDevelopmentalFabClicked()
        }

        binding.fabHumanitarian.setOnClickListener {
            viewModel.onHumanitarianFabClicked()
        }
        binding.textHumanitarianAction.setOnClickListener {
            viewModel.onHumanitarianFabClicked()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.householdsEventEvent.collect { event ->
                when (event) {
                    is HouseholdsViewModel.HouseholdEvent.AddRegistrationClicked -> {
                        if (!isOpen) showActions(binding) else hideActions(binding)
//                        val action = NonConsentingFragmentDirections.actionNavNonConsentingToNonConsentingFormFragment()
//                        findNavController().navigate(action)
                    }
                    is HouseholdsViewModel.HouseholdEvent.DevelopmentalClicked -> {
                        val action = HouseholdsFragmentDirections.actionNavHouseholdToDevelopmentalFormFragment()
                        findNavController().navigate(action)
                    }
                    is HouseholdsViewModel.HouseholdEvent.HumanitarianClicked -> {
                        val action = HouseholdsFragmentDirections.actionNavHouseholdToHumanitarianFormFragment()
                        findNavController().navigate(action)
                    }

                }
            }.exhaustive
        }


// set up menu
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_households_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.action_download_households -> {
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showActions(binding: FragmentHouseholdsBinding) {
        isOpen = true
        binding.fabAddRegistration.setImageResource(R.drawable.fab_close_24)
        binding.fabDevelopmental.show()
        binding.fabHumanitarian.show()
        binding.textAddRegistration.visibility = View.VISIBLE
        binding.textDevelopmentalAction.visibility = View.VISIBLE
        binding.textHumanitarianAction.visibility = View.VISIBLE
    }

    private fun hideActions(binding: FragmentHouseholdsBinding) {
        isOpen = false
        binding.fabAddRegistration.setImageResource(R.drawable.menu_add_24)
        binding.fabDevelopmental.hide()
        binding.fabHumanitarian.hide()
        binding.textAddRegistration.visibility = View.GONE
        binding.textDevelopmentalAction.visibility = View.GONE
        binding.textHumanitarianAction.visibility = View.GONE

    }
}