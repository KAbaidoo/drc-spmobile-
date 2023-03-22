package io.bewsys.spmobile.ui.households.forms.pages

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.bewsys.spmobile.PERMISSION_LOCATION_REQUEST_CODE
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdFivePropertyBinding
import io.bewsys.spmobile.ui.households.forms.SharedDevelopmentalFormViewModel
import io.bewsys.spmobile.ui.customviews.CustomQuestionViews
import io.bewsys.spmobile.util.LocationProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormStepFiveFragment : Fragment(R.layout.fragment_add_household_five_property),
    EasyPermissions.PermissionCallbacks  {
    private val sharedViewModel: SharedDevelopmentalFormViewModel by activityViewModel()
    private val locationProvider: LocationProvider by inject()
    private var currentLocation: Location? = null
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdFivePropertyBinding.bind(view)
        if (hasLocationPermission()) {
            locationProvider.getLocation().observe(viewLifecycleOwner) {
                currentLocation = it
            }
        } else {
            requestLocationPermission()
        }
        binding.apply {
            linearLayout.children.forEach { view ->

                val v = view as CustomQuestionViews

                v.answer = sharedViewModel.getEntry(v.title)

                v.addTextChangedListener {
                    if (it != null) {
                        sharedViewModel.saveEntry(v.title, it)
                    }
                }
            }

            buttonRegister.setOnClickListener {
                sharedViewModel.onRegisterClicked()
                getLastKnownLocation()
            }




        }
    }

    private fun getLastKnownLocation() {
        currentLocation?.apply {
            sharedViewModel.lon = longitude.toString()
            sharedViewModel.lat = latitude.toString()
//            Log.d("FormStepFive", "lon: ${it.longitude} lat: ${it.latitude}")
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