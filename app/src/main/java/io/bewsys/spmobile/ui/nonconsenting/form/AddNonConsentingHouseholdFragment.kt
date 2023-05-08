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
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.FusedLocationProviderClient

import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.bewsys.spmobile.FormNavigationArgs
import io.bewsys.spmobile.PERMISSION_LOCATION_REQUEST_CODE
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddNonConsentingBinding
import io.bewsys.spmobile.ui.MainActivity
import io.bewsys.spmobile.util.LocationProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import io.bewsys.spmobile.util.exhaustive
import io.bewsys.spmobile.util.swap
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject


class AddNonConsentingHouseholdFragment : Fragment(R.layout.fragment_add_non_consenting),
    EasyPermissions.PermissionCallbacks {

    val args: AddNonConsentingHouseholdFragmentArgs by navArgs()



    private val provinces = mutableListOf<String>()
    private val communities = mutableListOf<String>()
    private val territories = mutableListOf<String>()
    private val groupments = mutableListOf<String>()

    private val viewModel: AddNonConsentingHouseholdViewModel by viewModel()
    private var currentLocation: Location? = null
    private var locationProvider:LocationProvider?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         locationProvider =  LocationProvider(requireContext())

    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddNonConsentingBinding.bind(view)

        if (hasLocationPermission()) {
            locationProvider?.location?.observe(viewLifecycleOwner) { loc: Location? ->
                currentLocation = loc
            }
        } else {
            requestLocationPermission()
        }

        val household = args.household
        viewModel.household = household


        household?.let {
            viewModel.apply {
                viewModel.id = household?.id

                address = household?.address.toString()
                lon = household?.gps_longitude.toString()
                lat = household?.gps_latitude.toString()
               reason = household?.reason.toString()
                otherReason = household?.other_non_consent_reason.toString()
                province = household?.province_name.toString()
                territory = household?.territory_name.toString()
                community = household?.community_name.toString()
                groupment = household?.groupement_name.toString()

//          init  step five fields
            }
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

        val reasons = resources.getStringArray(R.array.reasons)


        binding.apply {


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
                    }
                }
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

        getLastKnownLocation()
    }//end of onCreateView

    private fun getLastKnownLocation() {
            Log.d(TAG, "lon: ${currentLocation?.longitude} lat: ${currentLocation?.latitude}")
        }


    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.cannot_work_without_permission),
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
    companion object {
        private const val TAG = "AddNonConsentingHousehold"
    }
}