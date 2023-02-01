package io.bewsys.spmobile.ui.nonconsenting.forms

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import io.bewsys.spmobile.databinding.FragmentNonConsentingFormBinding

class AddNonConsentingHouseholdFragment : Fragment(R.layout.fragment_non_consenting_form) {

    private val provinces = mutableListOf<String>()
    private val communities = mutableListOf<String>()

    private val viewModel: AddNonConsentingHouseholdViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentNonConsentingFormBinding.bind(view)

        viewModel.provinces.observe(viewLifecycleOwner) {
            it.map { provinceEntity ->
                provinceEntity.name?.let { name -> provinces.add(name) }
            }
        }
        viewModel.communities.observe(viewLifecycleOwner) {
            it.map { communityEntity ->
                communityEntity.name?.let { name -> communities.add(name) }
            }
        }

        val reasons = resources.getStringArray(R.array.reasons)
        val groupment = resources.getStringArray(R.array.groupment)
        val territories = resources.getStringArray(R.array.territories)
        val dropdownLayout = R.layout.dropdown_item_non_consent_form


        binding.apply {
//            Set all text fields from saved state
            textFieldReason.editText?.setText(viewModel.reason)
            textFieldProvince.editText?.setText(viewModel.province)
            textFieldCommunity.editText?.setText(viewModel.community)
            textFieldTerritory.editText?.setText(viewModel.territory)
            textFieldGroupment.editText?.setText(viewModel.groupment)
            textFieldOtherReason.editText?.setText(viewModel.otherReason)
            textFieldAddress.editText?.setText(viewModel.address)
            textFieldLon.editText?.setText(viewModel.lon)
            textFieldLat.editText?.setText(viewModel.lat)


            (autoCompleteTextViewReason as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, reasons).also {
                        setOnItemClickListener { _, _, _,_ ->
                            viewModel.reason = textFieldReason.editText?.text.toString()
                        }
                    }
                )
            }
            (autoCompleteTextViewProvince as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, provinces).also {
                        setOnItemClickListener { _, _, _, _ ->
                            viewModel.province = textFieldProvince.editText?.text.toString()
                            viewModel.getProvinceId()
                        }
                    }
                )
            }
            (autoCompleteTextViewCommunity as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, communities).also {
                        setOnItemClickListener { _, _, _, _ ->
                            viewModel.community = textFieldCommunity.editText?.text.toString()
                            viewModel.getCommunityId()
                        }
                    }
                )
            }
            (autoCompleteTextViewTerritory as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, territories)
                ).also {
                    setOnItemClickListener { _, _, _, _ ->
                        viewModel.territory = textFieldTerritory.editText?.text.toString()
                    }
                }
            }
            (autoCompleteTextGroupment as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, groupment)
                ).also {
                    setOnItemClickListener { _, _, _, _ ->
                        viewModel.groupment = textFieldGroupment.editText?.text.toString()
                    }
                }
            }
            textFieldOtherReason.editText?.doOnTextChanged{ text,_,_,_ ->
                viewModel.otherReason = text as String
            }
            textFieldAddress.editText?.doOnTextChanged{ text,_,_,_ ->
                viewModel.address = text as String
            }

            buttonRegister.setOnClickListener {

//                viewModel.lon = textFieldLon.editText?.text.toString()
//                viewModel.lat = textFieldLat.editText?.text.toString()

                viewModel.onRegisterClicked()
            }

        }

    }

}