package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdThreeHeadBinding
import io.bewsys.spmobile.databinding.FragmentAddHouseholdThreebMembersBinding
import io.bewsys.spmobile.ui.nonconsenting.NonConsentingHouseholdAdapter
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.*

class FormStepThreeBFragment : Fragment(R.layout.fragment_add_household_threeb_members) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdThreebMembersBinding.bind(view)

        val membersAdapter = MembersAdapter()

       binding.apply {
           viewLifecycleOwner.lifecycleScope.launchWhenStarted {
               viewModel.stepThreeHasBlankFields.collectLatest {
                   btnNext.isEnabled = it.not()
               }
           }
           recyclerViewMembers.apply {
               adapter = membersAdapter
               layoutManager = LinearLayoutManager(requireContext())
               setHasFixedSize(true)
           }

           viewModel.members.observe(viewLifecycleOwner){
               membersAdapter.submitList(it)
           }

         btnAddMembers.setOnClickListener {
             findNavController().navigate(R.id.membersFormFragment)
         }
            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepFourFragment,bundle )
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepThreeFragment,bundle )

            }

        }
        if(viewModel.household != null){
            viewModel.loadMembers()
        }
        // set up menu

    }
}