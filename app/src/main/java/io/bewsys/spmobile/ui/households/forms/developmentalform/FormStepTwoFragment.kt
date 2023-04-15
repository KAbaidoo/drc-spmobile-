package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
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
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import org.koin.androidx.navigation.koinNavGraphViewModel

import java.text.SimpleDateFormat
import java.util.*

class FormStepTwoFragment : Fragment(R.layout.fragment_add_household_two_respondent),
    EasyPermissions.PermissionCallbacks {
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

    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (hasLocationPermission()) {
            locationProvider.getLocation().observe(viewLifecycleOwner) {
                currentLocation = it
            }
        } else {
            requestLocationPermission()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddHouseholdTwoRespondentBinding.bind(view)
        viewModel.provinces.observe(viewLifecycleOwner) {
            provinces.clear()
            provinces.addAll(it)
        }
        viewModel.territories.observe(viewLifecycleOwner) {
            territories.clear()
            territories.addAll(it)
        }
        viewModel.communities.observe(viewLifecycleOwner) {
            communities.clear()
            communities.addAll(it)
        }
        viewModel.groupments.observe(viewLifecycleOwner) {
            groupments.clear()
            groupments.addAll(it)
        }
        val dropdownLayout = R.layout.dropdown_item





        binding.apply {
            til_Lat = tilLat
            til_Lon = tilLon

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.stepTwoHasBlankFields.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }


            val tils = listOf(
                tilRespondentFirstname,
                tilRespondentMiddlename,
                tilRespondentLastname,
                tilRespondentAge,
                tilRespondentVoterId,
                tilRespondentPhoneNumber,
                tilAddress,
            )


            tils.forEachIndexed { index, til ->
                til.editText?.setText(viewModel.stepTwoFields[index])
            }
            tilRespondentDob.editText?.setText(viewModel.respondentDOB)
            tilLon.editText?.setText(viewModel.lon)
            tilLat.editText?.setText(viewModel.lat)

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
            when (viewModel.areaOfResidence) {
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
                        viewModel.areaOfResidence = rbUrban.text.toString()
                    }
                    rbUrbanRural.id -> {
                        viewModel.areaOfResidence = rbUrbanRural.text.toString()
                    }
                    else -> {
                        viewModel.areaOfResidence = rbRural.text.toString()
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
                til.editText?.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        p0: CharSequence?,
                        p1: Int,
                        p2: Int,
                        p3: Int
                    ) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        viewModel.setStepTwoFields(index, p0)
                        viewModel.stepTwoHasBlankFields()
                        Log.d("FormStepTwo:", "$p0")
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }
                }
                )
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
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.format(it)
                tilRespondentDob.editText!!.setText(date)
                viewModel.respondentDOB = date
            }
            (autoCompleteTextViewProvince as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, provinces).also {
                        addTextChangedListener {
                            viewModel.province = it.toString()

                        }
                    }
                )
            }
            (autoCompleteTextViewCommunity as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, communities).also {
                        addTextChangedListener {
                            viewModel.community = it.toString()

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
                    ArrayAdapter(context, dropdownLayout, groupments)
                ).also {
                    addTextChangedListener {
                        viewModel.groupment = it.toString()
                    }
                }
            }
            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {

                val action =
                    FormStepTwoFragmentDirections.actionFormStepTwoFragmentToFormStepThreeFragment(
                        title = title,
                        household = viewModel.household
                    )
                findNavController().navigate(action)
            }
            btnPrevious.setOnClickListener {
                val action =
                    FormStepTwoFragmentDirections.actionFormStepTwoFragmentToFormStepOneFragment(
                        title = title,
                        household = viewModel.household
                    )
                findNavController().navigate(action)

            }


        } //end of apply block

        // set up menu
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_households_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.action_download_households -> {
                        val bundle = bundleOf("id" to viewModel.id)
                        findNavController().navigate(R.id.deleteHouseholdDialogFragment, bundle)

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


    }   //end of onCreate

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLastKnownLocation() {
        currentLocation?.apply {
            viewModel.lon = longitude.toString()
            viewModel.lat = latitude.toString()

            til_Lat?.editText?.setText(viewModel.lat)
            til_Lon?.editText?.setText(viewModel.lon)
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