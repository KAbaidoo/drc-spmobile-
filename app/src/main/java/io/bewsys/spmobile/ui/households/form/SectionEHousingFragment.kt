package io.bewsys.spmobile.ui.households.form

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
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel

import java.util.*

class SectionEHousingFragment : Fragment(R.layout.fragment_housing_e) {


    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)



    private var _binding: FragmentHousingEBinding? = null

    private val binding get() = _binding!!


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHousingEBinding.bind(view)
        val dropdownLayout = R.layout.dropdown_item

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.SectionEHasBlank.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }
            viewModel.apply {
                tilOccupancy.editText?.setText(occupancyStatus)
                tilExteriorWalls.editText?.setText(exteriorWalls)
                tilSoilMaterial.editText?.setText(soilMaterial)
                tilCookingFuel.editText?.setText(cookingFuel)
            }


            tilNumberOfRoomsUsedForSleeping.editText?.addTextChangedListener {
                viewModel.apply {
                    roomsUsedForSleeping = it.toString()
                    sectionEHasBlankFields()
                }
            }
            tilOtherOccupancyStatus.editText?.addTextChangedListener {
                viewModel.apply {
                    otherOccupancyStatus = it.toString()
                    sectionEHasBlankFields()
                }
            }
            tilOtherSoilMaterial.editText?.addTextChangedListener {
                viewModel.apply {
                    otherSoilMaterial = it.toString()
                    sectionEHasBlankFields()
                }
            }
            tilOtherExteriorWallMaterial.editText?.addTextChangedListener {
                viewModel.apply {
                    otherMaterialExterial = it.toString()
                    sectionEHasBlankFields()
                }
            }
            tilOtherCookingFuelType.editText?.addTextChangedListener {
                viewModel.apply {
                    otherCookingFuelType = it.toString()
                    sectionEHasBlankFields()
                }
            }

            val occupancyStatus = resources.getStringArray(R.array.occupancy)
            (actOccupancy as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, occupancyStatus).also {
                        addTextChangedListener {

                            viewModel.occupancyStatus = it.toString()
                           viewModel.sectionEHasBlankFields()


                        }
                    }
                )
            }
            val wallMaterial = resources.getStringArray(R.array.exterior_walls)
            (actExteriorWalls as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, wallMaterial).also {
                        addTextChangedListener {

                            viewModel.exteriorWalls = it.toString()
                            viewModel.sectionEHasBlankFields()

                        }
                    }
                )
            }
            val soilMaterial = resources.getStringArray(R.array.soil_material)
            (actSoilMaterial as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, soilMaterial).also {
                        addTextChangedListener {

                            viewModel.soilMaterial = it.toString()
                            viewModel.sectionEHasBlankFields()

                        }
                    }
                )
            }
            val cookingFuel = resources.getStringArray(R.array.cooking_fuel)
            (actCookingFuel as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, cookingFuel).also {
                        addTextChangedListener {

                            viewModel.cookingFuel = it.toString()
                            viewModel.sectionEHasBlankFields()

                        }
                    }
                )
            }

            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionFWaterAndSanitationFragment, bundle)
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionCIdentificationFragment, bundle)

            }

            viewModel.apply {
                tilOtherOccupancyStatus.editText?.setText(otherOccupancyStatus)
                tilOtherSoilMaterial.editText?.setText(otherSoilMaterial)
                tilOtherExteriorWallMaterial.editText?.setText(otherMaterialExterial)
                tilOtherCookingFuelType.editText?.setText(otherCookingFuelType)
                tilNumberOfRoomsUsedForSleeping.editText?.setText(roomsUsedForSleeping)

            }

        } //end of apply block

        viewModel.sectionEHasBlankFields()

    }   //end of onCreate

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

   }