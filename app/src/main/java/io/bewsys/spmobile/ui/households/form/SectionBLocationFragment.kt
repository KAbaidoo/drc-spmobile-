package io.bewsys.spmobile.ui.households.form

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
import com.google.android.material.textfield.TextInputLayout
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.bewsys.spmobile.PERMISSION_LOCATION_REQUEST_CODE
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentLocationOfHouseholdBBinding
import io.bewsys.spmobile.util.LocationProvider
import io.bewsys.spmobile.util.swap
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel

import java.util.*

class SectionBLocationFragment : Fragment(R.layout.fragment_location_of_household_b),
    EasyPermissions.PermissionCallbacks {


    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    private val provinces = mutableListOf<String>()
    private val communities = mutableListOf<String>()
    private val territories = mutableListOf<String>()
    private val groupments = mutableListOf<String>()
    private val healthZones = mutableListOf<String>()
    private val healthAreas = mutableListOf<String>()

    private var til_Lat: TextInputLayout? = null
    private var til_Lon: TextInputLayout? = null

    private var currentLocation: Location? = null

    private var _binding: FragmentLocationOfHouseholdBBinding? = null
    private var locationProvider: LocationProvider ? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationProvider = LocationProvider(requireActivity())
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLocationOfHouseholdBBinding.bind(view)

        if (hasLocationPermission()) {
            locationProvider?.getLocation {
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
        viewModel.healthZones.observe(viewLifecycleOwner) {
            healthZones.swap(it)
        }
        viewModel.healthAreas.observe(viewLifecycleOwner) {
            healthAreas.swap(it)
        }
        val dropdownLayout = R.layout.dropdown_item


        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.SectionBHasBlank.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }
            tilVillageDistrict.editText?.addTextChangedListener {
                viewModel.villageOrDistrict = it.toString()
                viewModel.sectionBHasBlankFields()
            }
            tilAddress.editText?.addTextChangedListener {
                viewModel.address = it.toString()
                viewModel.sectionBHasBlankFields()
                getLastKnownLocation()
            }
            tilCac.editText?.addTextChangedListener {
                viewModel.cac = it.toString()
                viewModel.sectionBHasBlankFields()
                getLastKnownLocation()
            }
            til_Lat = tilLat
            til_Lon = tilLon

            when (viewModel.registrationType) {
                rbGeneral.text -> rgInitialRegistrationType.check(rbGeneral.id)

                rbEmergency.text -> rgInitialRegistrationType.check(rbEmergency.id)
            }

            when (viewModel.placeOfResidence) {
                rbUrban.text -> rgAreaOfResidence.check(rbUrban.id)
                rbUrbanRural.text -> rgAreaOfResidence.check(rbUrbanRural.id)
                rbRural.text -> rgAreaOfResidence.check(rbRural.id)
            }
            viewModel.apply {
                tilVillageDistrict.editText?.setText(villageOrDistrict)
                tilCac.editText?.setText(cac)
                tilAddress.editText?.setText(address)
                tilProvince.editText?.setText(province)
                tilCommunity.editText?.setText(community)
                tilTerritory.editText?.setText(territory)
                tilGroupment.editText?.setText(groupment)
                tilHealthArea.editText?.setText(healthArea)
                tilHealthZone.editText?.setText(healthZone)
            }

            rgInitialRegistrationType.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbGeneral.id -> {
                        viewModel.registrationType = rbGeneral.text.toString()
                        getLastKnownLocation()
                    }

                    else -> {
                        viewModel.registrationType = rbEmergency.text.toString()
                        getLastKnownLocation()
                    }
                }
            }

            rgAreaOfResidence.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbUrban.id -> {
                        viewModel.placeOfResidence = rbUrban.text.toString()
                        getLastKnownLocation()
                    }

                    rbUrbanRural.id -> {
                        viewModel.placeOfResidence = rbUrbanRural.text.toString()
                        getLastKnownLocation()
                    }

                    else -> {
                        viewModel.placeOfResidence = rbRural.text.toString()
                    }
                }
            }


            tilAddress.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.address.isBlank()) {
                    tilAddress.error = getString(R.string.field_cannot_be_empty)
                } else tilAddress.error = null
            }
            tilCac.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.cac.isBlank()) {
                    tilCac.error = getString(R.string.field_cannot_be_empty)
                } else tilCac.error = null
            }
            tilVillageDistrict.editText?.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus && viewModel.villageOrDistrict.isBlank()) {
                    tilVillageDistrict.error = getString(R.string.field_cannot_be_empty)
                } else tilVillageDistrict.error = null
            }


            (autoCompleteTextViewProvince as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, provinces).also {
                        addTextChangedListener {
                            viewModel.apply {
                                province = it.toString()
                                loadTerritoriesWithName(it.toString())
                                sectionBHasBlankFields()
                            }
                            getLastKnownLocation()
                        }
                    }
                )
            }

            (autoCompleteTextViewCommunity as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, communities)
                        .also {
                            addTextChangedListener {
                                viewModel.apply {
                                    community = it.toString()
                                    loadGroupmentsWithName(it.toString())
                                    sectionBHasBlankFields()
                                }
                                getLastKnownLocation()

                            }
                        }
                )
            }

            (autoCompleteTextViewTerritory as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, territories)
                ).also {
                    addTextChangedListener {
                        viewModel.apply {
                            territory = it.toString()
                            loadCommunitiesWithName(it.toString())
                            sectionBHasBlankFields()
                        }
                    }
                }
            }

            (autoCompleteTextGroupment as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, groupments)
                ).also {
                    addTextChangedListener {
                        viewModel.apply {
                            groupment = it.toString()
                            getGroupmentId(it.toString())
                            sectionBHasBlankFields()
                        }
                    }
                    getLastKnownLocation()
                }
            }

            (actHealthZone as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, healthZones)
                ).also {
                    addTextChangedListener {
                        viewModel.apply {
                            healthZone = it.toString()
                            loadHealthAreasWithName()
                            sectionBHasBlankFields()
                        }

                    }
                }
            }

            (actHealthArea as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, healthAreas)
                ).also {
                    addTextChangedListener {
                        viewModel.apply {
                            healthArea = it.toString()
                            getHealthAreaId()
                            sectionBHasBlankFields()
                        }

                    }
                }
            }

            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionCIdentificationFragment, bundle)
            }
            btnPrevious.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepOneFragment, bundle)

            }


        } //end of apply block

        viewModel.sectionBHasBlankFields()
        getLastKnownLocation()
    }   //end of onCreate

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        locationProvider?.removeLocationUpdates()

    }

    private fun getLastKnownLocation() {
        currentLocation?.let {
           val lon = it.longitude.toString()
            val lat = it.latitude.toString()

            viewModel.lon = lon
            viewModel.lat = lat

            val trimmedLon = if(lon.length > 7) lon.subSequence(0,7) else lon
            val trimmedLat = if(lat.length > 7) lon.subSequence(0,7) else lat

            til_Lat?.editText?.setText(trimmedLon)
            til_Lon?.editText?.setText(trimmedLat)
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