package io.bewsys.spmobile.ui.nonconsenting.form

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddNonConsentingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import io.bewsys.spmobile.util.exhaustive

class AddNonConsentingHouseholdFragment : Fragment(R.layout.fragment_add_non_consenting) {

    private val provinces = mutableListOf<String>()
    private val communities = mutableListOf<String>()

    private val viewModel: AddNonConsentingHouseholdViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding =FragmentAddNonConsentingBinding.bind(view)

        //TODO Refactor to use Paired PairMediatorLiveData
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
        val dropdownLayout = R.layout.dropdown_item


        binding.apply {
//            Set all text fields from saved state
            textFieldReason.editText?.setText(viewModel.reason)
            textFieldProvince.editText?.setText(viewModel.province)
            textFieldCommunity.editText?.setText(viewModel.community)
            textFieldTerritory.editText?.setText(viewModel.territory)
            textFieldGroupment.editText?.setText(viewModel.groupment)
            textFieldOtherReason.editText?.setText(viewModel.otherReason)
            textFieldLon.editText?.setText(viewModel.lon)
            textFieldLat.editText?.setText(viewModel.lat)


            (autoCompleteTextViewReason as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, reasons).also {
                        addTextChangedListener {
                            viewModel.reason = it.toString()
                        }
                    }
                )
            }
            (autoCompleteTextViewProvince as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, provinces).also {
                        addTextChangedListener {
                            viewModel.province = it.toString()
                            viewModel.getProvinceId()
                        }
                    }
                )
            }
            (autoCompleteTextViewCommunity as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, communities).also {
                        addTextChangedListener {
                            viewModel.community = it.toString()
                            viewModel.getCommunityId()
                        }
                    }
                )
            }
            (autoCompleteTextViewTerritory as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, territories)
                ).also {
                    addTextChangedListener {
                        viewModel.territory = it.toString()
                    }
                }
            }
            (autoCompleteTextGroupment as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, groupment)
                ).also { addTextChangedListener {
                        viewModel.groupment = it.toString()
                    }
                }
            }
            textFieldOtherReason.editText?.addTextChangedListener {
                viewModel.reason = it.toString()
            }


            textFieldOtherReason.editText?.addTextChangedListener {
                viewModel.otherReason = it.toString()
            }

            buttonRegister.setOnClickListener {

//                viewModel.lon = textFieldLon.editText?.text.toString()
//                viewModel.lat = textFieldLat.editText?.text.toString()

                viewModel.onRegisterClicked()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addNonConsentingHouseholdEvent.collect{
                event ->
                when (event){
                    is AddNonConsentingHouseholdViewModel.AddNonConsentingHouseholdEvent.ShowInvalidInputMessage ->

                        Snackbar.make(requireView(),event.msg, Snackbar.LENGTH_LONG).show()

                    is AddNonConsentingHouseholdViewModel.AddNonConsentingHouseholdEvent.NavigateBackWithResults -> {

                        setFragmentResult(
                            "add_non_consenting_household_request",
                            bundleOf("add_non_consenting_household_result" to event.results)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }

    }

}