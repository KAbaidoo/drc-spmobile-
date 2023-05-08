package io.bewsys.spmobile.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.os.LocaleListCompat
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.*
import androidx.preference.PreferenceManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.material.navigation.NavigationView
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import io.bewsys.spmobile.PERMISSION_LOCATION_REQUEST_CODE
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.ActivityMainBinding
import io.bewsys.spmobile.ui.nonconsenting.form.AddNonConsentingHouseholdFragment
import io.bewsys.spmobile.util.LocalizationUtil
import io.bewsys.spmobile.util.LocationProvider
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var locationProvider : LocationProvider? = null
//    private val locationProvider : LocationProvider by inject()


    private val viewModel: MainViewModel by viewModel()

    private var currentLocation: Location? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      locationProvider =  LocationProvider(this)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


//        lifecycleScope.launchWhenStarted {
//            viewModel.userState.collectLatest {
//                if (it.not()) {
//                    navController.navigate(R.id.nav_login)
//                }
//            }
//        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView


        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_household,
                R.id.nav_non_consenting
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navView.setOnClickListener { menuItem ->
            when (menuItem.id) {
                R.id.nav_profile -> navController.navigate(R.id.nav_profile)
                R.id.nav_settings -> navController.navigate(R.id.nav_settings)

                R.id.nav_login -> {
                    navController.navigate(R.id.nav_login)

                }
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.nav_login -> {
                    drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
                    binding.appBarMain.toolbar.visibility = View.GONE
                }
                else ->
                    binding.appBarMain.toolbar.visibility = View.VISIBLE
            }
        }

        @SuppressLint("MissingPermission")
        if (hasLocationPermission()) {
            locationProvider?.location?.observe(this) { loc: Location? ->
                currentLocation = loc
            }
        } else {
            requestLocationPermission()
        }


        getLastKnownLocation()
    } //end of onCreate

    private fun getLastKnownLocation() {

        Log.d(TAG, "lon: ${currentLocation?.longitude} lat: ${currentLocation?.latitude}")

    }

    private fun hasLocationPermission() =
        EasyPermissions.hasPermissions(
            applicationContext,
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
            SettingsDialog.Builder(applicationContext).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            applicationContext,
            getString(R.string.permission_granted),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
