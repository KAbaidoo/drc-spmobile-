package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.os.bundleOf

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.bewsys.spmobile.PERMISSION_LOCATION_REQUEST_CODE
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdTwoRespondentBinding
import io.bewsys.spmobile.util.LocationProvider
import io.bewsys.spmobile.util.swap
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel

import java.text.SimpleDateFormat
import java.util.*

class FormStepTwoFragment : Fragment(R.layout.fragment_add_household_two_respondent),
    EasyPermissions.PermissionCallbacks {


    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    private val provinces = mutableListOf<String>()
    private val communities = mutableListOf<String>()
    private val territories = mutableListOf<String>()

    private val groupments = mutableListOf<String>()
    private var til_Lat: TextInputLayout? = null

    private var til_Lon: TextInputLayout? = null
    private var currentLocation: Location? = null

    private val locationProvider: LocationProvider by inject()

    private var _binding: FragmentAddHouseholdTwoRespondentBinding? = null

    private val binding get() = _binding!!




    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddHouseholdTwoRespondentBinding.bind(view)


        if (hasLocationPermission()) {
            locationProvider.location.observe(viewLifecycleOwner) {
                currentLocation = it
            }
        } else {
            requestLocationPermission()
        }


        viewModel.provinces.observe(viewLifecycleOwner) {
            provinces.swap(it)
        }
        viewModel.territories.observe(viewLifecycleOwner) {
            territories.swap(it)
        }
        viewModel.communities.observe(viewLifecycleOwner) {

            communities.swap(it)
        }
        viewModel.groupments.observe(viewLifecycleOwner) {
            groupments.swap(it)
        }
        val dropdownLayout = R.layout.dropdown_item




        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.stepTwoHasBlankFields.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }
            til_Lat = tilLat
            til_Lon = tilLon

            val tils = listOf(
                tilRespondentFirstname,
                tilRespondentMiddlename,
                tilRespondentLastname,
                tilRespondentAge,
                tilRespondentVoterId,
                tilRespondentPhoneNumber,
                tilAddress
            )



            rgInitialRegistrationType.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbGeneral.id -> {
                        viewModel.initialRegistrationType = rbGeneral.text as String
                        getLastKnownLocation()
                    }
                    else -> {
                        viewModel.initialRegistrationType = rbEmergency.text as String
                        getLastKnownLocation()

                    }
                }

            }
            rgFamilyBondToHead.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYes.id -> {
                        viewModel.respondentFamilyBondToHead = rbYes.text.toString()
                    }
                    else -> {
                        viewModel.respondentFamilyBondToHead = rbNo.text.toString()
                    }
                }

            }
            rgRespondentSex.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbMale.id -> {
                        viewModel.respondentSex = rbMale.text.toString()
                    }
                    else -> {
                        viewModel.respondentSex = rbFemale.text.toString()
                    }
                }

            }
            rgVillageOrQuartier.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbVillage.id -> {
                        viewModel.villageOrQuartier = rbVillage.text.toString()
                    }
                    else -> {
                        viewModel.villageOrQuartier = rbQuartier.text.toString()
                    }
                }

            }
            rgTerritoryOrTown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbTerritory.id -> {
                        viewModel.territoryOrTown = rbTerritory.text.toString()
                    }
                    else -> {
                        viewModel.territoryOrTown = rbTown.text.toString()
                    }
                }

            }
            rgAreaOfResidence.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbUrban.id -> {
                        viewModel.placeOfResidence = rbUrban.text.toString()
                    }
                    rbUrbanRural.id -> {
                        viewModel.placeOfResidence = rbUrbanRural.text.toString()
                    }
                    else -> {
                        viewModel.placeOfResidence = rbRural.text.toString()
                    }
                }

            }
            rgRespondentAgeKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAge.id -> {
                        viewModel.respondentAgeKnown = rbYesAge.text.toString()
                        tvRespondentAge.isEnabled = true
                        tilRespondentAge.isEnabled = true
                    }
                    else -> {
                        viewModel.respondentAgeKnown = rbNoAge.text.toString()
                        tvRespondentAge.isEnabled = false
                        tilRespondentAge.isEnabled = false
                        tilRespondentAge.editText?.text?.clear()
                    }
                }

            }
            rgRespondentDobKnown.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesDob.id -> {
                        viewModel.respondentDOBKnown = rbYesDob.text.toString()
                        tvRespondentDob.isEnabled = true
                        tilRespondentDob.isEnabled = true
                    }
                    else -> {
                        viewModel.respondentDOBKnown = rbNoDob.text.toString()
                        tvRespondentDob.isEnabled = false
                        tilRespondentDob.isEnabled = false
                        tilRespondentDob.editText?.text?.clear()
                    }
                }

            }



            tils.forEachIndexed { index, til ->
                til.editText?.addTextChangedListener {
                    viewModel.setStepTwoFields(index, it)
                    viewModel.stepTwoHasBlankFields()
                }

            }

            tilRespondentFirstname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.respondentFirstName.isBlank()) {
                    tilRespondentFirstname.error = getString(R.string.field_cannot_be_empty)
                } else tilRespondentFirstname.error = null
            }
            tilRespondentLastname.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.respondentLastName.isBlank()) {
                    tilRespondentLastname.error = getString(R.string.field_cannot_be_empty)
                } else tilRespondentLastname.error = null
            }






            val datePicker = MaterialDatePicker
                .Builder
                .datePicker()
                .setTitleText(getString(R.string.select_dob))
                .build()
            tilRespondentDob.editText?.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) datePicker.show(parentFragmentManager, "DATE_PICKER")
            }
            tilRespondentDob.editText?.setOnClickListener {
                datePicker.show(parentFragmentManager, "DATE_PICKER")
            }
            datePicker.addOnPositiveButtonClickListener {
                val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = sdf.format(it)
                tilRespondentDob.editText!!.setText(date)
                viewModel.respondentDOB = date
            }
            (autoCompleteTextViewProvince as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, provinces).also {
                        addTextChangedListener {

                            viewModel.province = it.toString()
                            viewModel.loadTerritoriesWithName(it.toString())

                        }
                    }
                )
            }
            (autoCompleteTextViewCommunity as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, communities).also {
                        addTextChangedListener {
                            viewModel.community = it.toString()
                            viewModel.loadGroupmentsWithName(it.toString())

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
                        viewModel.loadCommunitiesWithName(it.toString())
                    }
                }
            }
            (autoCompleteTextGroupment as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, groupments)
                ).also {
                    addTextChangedListener {

                        viewModel.groupment = it.toString()
                        viewModel.getGroupmentId(it.toString())
                    }
                }
            }
            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepThreeFragment, bundle)
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepOneFragment, bundle)

            }

            viewModel.apply {
                tilRespondentFirstname.editText?.setText(respondentFirstName)
                tilRespondentMiddlename.editText?.setText(respondentMiddleName)
                tilRespondentLastname.editText?.setText(respondentLastName)
                tilRespondentAge.editText?.setText(respondentAge)
                tilRespondentVoterId.editText?.setText(respondentVoterId)
                tilRespondentPhoneNumber.editText?.setText(respondentPhoneNo)
                tilAddress.editText?.setText(address)
            }


            tilRespondentDob.editText?.setText(viewModel.respondentDOB)
            tilLon.editText?.setText(viewModel.lon)
            tilLat.editText?.setText(viewModel.lat)
//            tils.forEachIndexed { index, til ->
//                til.editText?.setText(viewModel.stepTwoFields[index])
//            }
            if(viewModel.household == null){
                rgRespondentDobKnown.check(rbYesDob.id)
                rgRespondentAgeKnown.check(rbYesAge.id)
            }

            when (viewModel.initialRegistrationType) {
                rbGeneral.text -> rgInitialRegistrationType.check(rbGeneral.id)
                rbEmergency.text -> rgInitialRegistrationType.check(rbEmergency.id)
            }
            when (viewModel.respondentFamilyBondToHead) {
                rbYes.text -> rgFamilyBondToHead.check(rbYes.id)
                rbNo.text -> rgFamilyBondToHead.check(rbNo.id)
            }
            when (viewModel.villageOrQuartier) {
                rbVillage.text -> rgVillageOrQuartier.check(rbVillage.id)
                rbQuartier.text -> rgVillageOrQuartier.check(rbQuartier.id)
            }
            when (viewModel.territoryOrTown) {
                rbTerritory.text -> rgTerritoryOrTown.check(rbTerritory.id)
                rbTown.text -> rgTerritoryOrTown.check(rbTown.id)
            }
            when (viewModel.placeOfResidence) {
                rbUrban.text -> rgAreaOfResidence.check(rbUrban.id)
                rbUrbanRural.text -> rgAreaOfResidence.check(rbUrbanRural.id)
                rbRural.text -> rgAreaOfResidence.check(rbRural.id)
            }
            when (viewModel.respondentAgeKnown) {
                rbYesAge.text -> rgRespondentAgeKnown.check(rbYesAge.id)
                rbNoAge.text -> rgRespondentAgeKnown.check(rbNoAge.id)
            }
            when (viewModel.respondentDOBKnown) {
                rbYesDob.text -> rgRespondentDobKnown.check(rbYesDob.id)
                rbNoDob.text -> rgRespondentDobKnown.check(rbNoDob.id)
            }
            when (viewModel.respondentSex) {
                rbMale.text -> rgRespondentSex.check(rbMale.id)
                rbFemale.text -> rgRespondentSex.check(rbFemale.id)
            }



        } //end of apply block


        getLastKnownLocation()
    }   //end of onCreate

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLastKnownLocation() {
        currentLocation?.apply {
            viewModel.lon = longitude.toString()
            viewModel.lat = latitude.toString()

            til_Lat?.editText?.setText(latitude.toString())
            til_Lon?.editText?.setText(longitude.toString())
        }
    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.cannot_work_without_location_permission),
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionDenied(this, perms.first())) {
            SettingsDialog.Builder(requireContext()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            getString(R.string.permission_granted),
            Toast.LENGTH_SHORT
        ).show()
    }

}