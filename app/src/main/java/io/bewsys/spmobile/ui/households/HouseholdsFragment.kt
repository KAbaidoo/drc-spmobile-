package io.bewsys.spmobile.ui.households

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentHouseholdsBinding
import org.w3c.dom.Text

class HouseholdsFragment : Fragment(R.layout.fragment_households) {
    val viewModel: HouseholdsViewModel by viewModels()
    var isOpen:Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHouseholdsBinding.bind(view)



        hideActions(binding)
        binding.fabAddRegistration.setOnClickListener {
            if (!isOpen) showActions(binding) else hideActions(binding)
            isOpen = !isOpen
        }



        binding.fabDevelopmental.setOnClickListener{
            TODO("implement snackbar")

        }
        binding.fabHumanitarian.setOnClickListener{
            TODO("implement snackbar")
        }




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
        binding.fabAddRegistration.setImageResource(R.drawable.fab_close_24)
        binding.fabDevelopmental.show()
        binding.fabHumanitarian.show()
        binding.textAddRegistration.visibility = View.VISIBLE
        binding.textDevelopmentalAction.visibility = View.VISIBLE
        binding.textHumanitarianAction.visibility = View.VISIBLE
    }

    private fun hideActions(binding: FragmentHouseholdsBinding) {
        binding.fabAddRegistration.setImageResource(R.drawable.menu_add_24)
        binding.fabDevelopmental.hide()
        binding.fabHumanitarian.hide()
        binding.textAddRegistration.visibility = View.GONE
        binding.textDevelopmentalAction.visibility = View.GONE
        binding.textHumanitarianAction.visibility = View.GONE

    }
}