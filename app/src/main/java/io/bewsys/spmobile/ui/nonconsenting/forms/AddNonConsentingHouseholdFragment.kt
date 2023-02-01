package io.bewsys.spmobile.ui.nonconsenting.forms

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentNonConsentingFormBinding

class AddNonConsentingHouseholdFragment : Fragment(R.layout.fragment_non_consenting_form) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNonConsentingFormBinding.bind(view)

//        TODO("refactor")
        val reasons = resources.getStringArray(R.array.reasons)
        val provinces = resources.getStringArray(R.array.provinces)
        val communities = resources.getStringArray(R.array.community)
        val groupment = resources.getStringArray(R.array.groupment)
        val territories = resources.getStringArray(R.array.territories)
        val dropdownLayout =  R.layout.dropdown_item_non_consent_form


        binding.apply {
            (autoCompleteTextViewReason as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, reasons)
                )
            }

            (autoCompleteTextViewProvince as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context,dropdownLayout, provinces)
                )
            }
            (autoCompleteTextViewTerritory as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context,dropdownLayout, territories)
                )
            }
            (autoCompleteTextViewCommunity as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context,dropdownLayout, communities)
                )
            }
            (autoCompleteTextGroupment as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context,dropdownLayout, groupment)
                )
            }


        }













    }


}