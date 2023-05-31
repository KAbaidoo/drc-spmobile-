package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf

import androidx.fragment.app.Fragment

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import io.bewsys.spmobile.R

import io.bewsys.spmobile.databinding.FragmentCompositionOfHouseholdDBinding

import org.koin.androidx.navigation.koinNavGraphViewModel


class SectionDCompositionOfHousholdFragment : Fragment(R.layout.fragment_composition_of_household_d) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentCompositionOfHouseholdDBinding.bind(view)

        val membersAdapter = MembersAdapter()

       binding.apply {

           recyclerViewMembers.apply {
               adapter = membersAdapter
               layoutManager = LinearLayoutManager(requireContext())
               setHasFixedSize(true)
           }

           viewModel.members.observe(viewLifecycleOwner){
               membersAdapter.submitList(it)
           }

         btnAddMembers.setOnClickListener {
             findNavController().navigate(R.id.sectionDAddMemberFragment)
         }

            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionEHousingFragment,bundle )
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionCIdentificationFragment,bundle )

            }

        }
        if(viewModel.household != null){
            viewModel.loadMembers()
        }
        // set up menu

    }
}