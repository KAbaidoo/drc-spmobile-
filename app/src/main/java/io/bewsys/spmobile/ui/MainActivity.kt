package io.bewsys.spmobile.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.os.LocaleListCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.*
import androidx.preference.Preference
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
import io.bewsys.spmobile.databinding.NavHeaderMainBinding
import io.bewsys.spmobile.ui.nonconsenting.form.AddNonConsentingHouseholdFragment
import io.bewsys.spmobile.util.LocationProvider
import io.bewsys.spmobile.util.MapUtil
import io.bewsys.spmobile.util.getPreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {


    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    //============================

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private var mTime: Long = 1_800_000

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initializing the handler and the runnable
        mHandler = Handler(Looper.getMainLooper())

        mRunnable = Runnable {

            navController.navigate(R.id.nav_login)

            Toast.makeText(
                applicationContext,
                "User inactive for ${mTime / 60_000} mins!",
                Toast.LENGTH_SHORT
            ).show()
        }




        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)


        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val header: View = navigationView.getHeaderView(0)
        val tv: TextView = header.findViewById(R.id.tv_username)

        lifecycleScope.launchWhenStarted {
            viewModel.userState.collectLatest {
                if (it.not()) {
                    navController.navigate(R.id.nav_login)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.getUser().collectLatest {
                tv.text = it.name ?: "username"
            }
        }


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

        // Start the handler
        startHandler()
    } //end of onCreate


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {

        // Removes the handler callbacks (if any)
        stopHandler()

        // Runs the handler (for the specified time)
        // If any touch or motion is detected before
        // the specified time, this override function is again called
        startHandler()
        return super.dispatchTouchEvent(ev)
    }

    // start handler function
    private fun startHandler() {
        mHandler.postDelayed(mRunnable, mTime)
    }

    // stop handler function
    private fun stopHandler() {
        mHandler.removeCallbacks(mRunnable)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}
