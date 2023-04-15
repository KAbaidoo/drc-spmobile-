package io.bewsys.spmobile.ui.nonconsenting.form

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext


import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment

import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.bewsys.spmobile.PERMISSION_LOCATION_REQUEST_CODE
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddNonConsentingBinding
import io.bewsys.spmobile.util.LocationProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import io.bewsys.spmobile.util.exhaustive
import org.koin.android.ext.android.inject


class AddNonConsentingHouseholdFragment : Fragment(R.layout.fragment_add_non_consenting),
    EasyPermissions.PermissionCallbacks {

    private var currentLocation: Location? = null

    private val provinces = mutableListOf<String>()
    private val communities = mutableListOf<String>()
    private val territories = mutableListOf<String>()
    private val groupments = mutableListOf<String>()

    private val viewModel: AddNonConsentingHouseholdViewModel by viewModel()
    private val locationProvider: LocationProvider by inject()


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddNonConsentingBinding.bind(view)

        if (hasLocationPermission()) {
            locationProvider.getLocation().observe(viewLifecycleOwner) {
                currentLocation = it
            }
        } else {
            requestLocationPermission()
        }

        //TODO Refactor to use Paired PairMediatorLiveData
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

        val reasons = resources.getStringArray(R.array.reasons)


        binding.apply {

            //            Set all text fields from saved state
            textFieldReason.editText?.setText(viewModel.reason)
            textFieldProvince.editText?.setText(viewModel.province)
            textFieldCommunity.editText?.setText(viewModel.community)
            textFieldTerritory.editText?.setText(viewModel.territory)
            textFieldGroupment.editText?.setText(viewModel.groupment)
            textFieldOtherReason.editText?.setText(viewModel.otherReason)
            textFieldAddress.editText?.setText(viewModel.address)

            (autoCompleteTextViewReason as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, reasons).also {
                        addTextChangedListener {
                            val reason = it.toString()
                            viewModel.reason = reason
                            textFieldOtherReason.isEnabled  = reason == getString(R.string.other)
                            textViewOtherReason.isEnabled  = reason == getString(R.string.other)
                        }
                    }
                )
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





            textFieldOtherReason.editText?.addTextChangedListener {
                viewModel.reason = it.toString()
            }

            textFieldOtherReason.editText?.addTextChangedListener {
                viewModel.otherReason = it.toString()
            }

            textFieldAddress.editText?.addTextChangedListener {
                viewModel.address = it.toString()
                getLastKnownLocation()
            }
            buttonRegister.setOnClickListener {
                viewModel.onSaveClicked()

            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addNonConsentingHouseholdEvent.collect { event ->
                when (event) {
                    is AddNonConsentingHouseholdViewModel.AddNonConsentingHouseholdEvent.ShowInvalidInputMessage ->

                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()

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


    }//end of onCreateView

    private fun getLastKnownLocation() {
        currentLocation?.apply {
           viewModel.lon = longitude.toString()
           viewModel.lat = latitude.toString()
//            Log.d("AddNonConcent", "lon: ${viewModel.lon} lat: ${viewModel.lat}")
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
            "This application cannot work without Location Permission.",
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
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
    }
}