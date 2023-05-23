package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentHousingEBinding
import io.bewsys.spmobile.databinding.FragmentWaterAndSanitationFBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel

import java.util.*

class SectionFWaterAndSanitationFragment : Fragment(R.layout.fragment_water_and_sanitation_f) {


    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    private var _binding: FragmentWaterAndSanitationFBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentWaterAndSanitationFBinding.bind(view)
        val dropdownLayout = R.layout.dropdown_item

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.SectionFHasBlank.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }
            viewModel.apply {
                tilSourceOfDrinkingWater.editText?.setText(sourceOfDrinkingWater)
                tilWasteDisposal.editText?.setText(wasteDisposal)
                tilPlaceForHandWashing.editText?.setText(placeForHandWashing)
                tilTypeOfToilet.editText?.setText(typeOfToilet)
            }


            tilOtherSourceOfDrinkingWater.editText?.addTextChangedListener {
                viewModel.apply {
                    otherSourceOfDrinkingWater = it.toString()
                    sectionFHasBlankFields()
                }
            }
            tilOtherTypeOfToilet.editText?.addTextChangedListener {
                viewModel.apply {
                    otherTypeOfToilet = it.toString()
                    sectionFHasBlankFields()
                }
            }
            tilOtherWasteDisposal.editText?.addTextChangedListener {
                viewModel.apply {
                    otherWasteDisposal = it.toString()
                    sectionFHasBlankFields()
                }
            }


            val sourceOfDrinkingWater = resources.getStringArray(R.array.source_of_drinking_water)
            (actSourceOfDrinkingWater as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, sourceOfDrinkingWater).also {
                        addTextChangedListener {

                            viewModel.sourceOfDrinkingWater = it.toString()
                           viewModel.sectionFHasBlankFields()


                        }
                    }
                )
            }
            val typeOfToilet = resources.getStringArray(R.array.type_of_toilet)
            (actTypeOfToilet as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, typeOfToilet).also {
                        addTextChangedListener {

                            viewModel.typeOfToilet = it.toString()
                            viewModel.sectionFHasBlankFields()

                        }
                    }
                )
            }
            val wasteDisposal = resources.getStringArray(R.array.waste_disposal_methods)
            (actWasteDisposal as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, wasteDisposal).also {
                        addTextChangedListener {

                            viewModel.wasteDisposal = it.toString()
                            viewModel.sectionFHasBlankFields()

                        }
                    }
                )
            }
            val placeForHandWashing = resources.getStringArray(R.array.place_of_hand_washing)
            (actPlaceForHandWashing as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, placeForHandWashing).also {
                        addTextChangedListener {

                            viewModel.placeForHandWashing = it.toString()
                            viewModel.sectionFHasBlankFields()

                        }
                    }
                )
            }


            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionGIncomeFragment, bundle)
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionEHousingFragment, bundle)

            }

            viewModel.apply {
                tilOtherSourceOfDrinkingWater.editText?.setText(otherSourceOfDrinkingWater)
                tilOtherTypeOfToilet.editText?.setText(otherTypeOfToilet)
                tilOtherWasteDisposal.editText?.setText(otherWasteDisposal)
            }

        } //end of apply block


        viewModel.sectionFHasBlankFields()
    }   //end of onCreate

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   }