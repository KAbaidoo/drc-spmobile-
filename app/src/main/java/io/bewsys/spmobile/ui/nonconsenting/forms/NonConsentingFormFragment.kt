package io.bewsys.spmobile.ui.nonconsenting.forms

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentNonConsentingFormBinding

class NonConsentingFormFragment :Fragment (R.layout.fragment_non_consenting_form){


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNonConsentingFormBinding.bind(view)

        val reasons = resources.getStringArray(R.array.reasons)
        val reasonsAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_non_consent_form, reasons)
        (binding.autoCompleteTextViewReason as? AutoCompleteTextView)?.setAdapter(reasonsAdapter)

        val provinces = resources.getStringArray(R.array.provinces)
        val provinceAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_non_consent_form, provinces)
        (binding.autoCompleteTextViewProvince as? AutoCompleteTextView)?.setAdapter(provinceAdapter)

        val territories = resources.getStringArray(R.array.territories)
        val territoriesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_non_consent_form, territories)
        (binding.autoCompleteTextViewTerritory as? AutoCompleteTextView)?.setAdapter(territoriesAdapter)


        val municipality = resources.getStringArray(R.array.municipality)
        val municipalityAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_non_consent_form, municipality)
        (binding.autoCompleteTextViewMunicipality as? AutoCompleteTextView)?.setAdapter(municipalityAdapter)

        val groupment = resources.getStringArray(R.array.groupment)
        val groupmentAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item_non_consent_form, groupment)
        (binding.autoCompleteTextGroupment as? AutoCompleteTextView)?.setAdapter(groupmentAdapter)
    }
}