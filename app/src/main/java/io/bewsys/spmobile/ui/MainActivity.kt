package io.bewsys.spmobile.ui

import android.app.Activity
import android.os.Bundle
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController

import androidx.navigation.findNavController
import androidx.navigation.ui.*
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.ActivityMainBinding
import io.bewsys.spmobile.ui.settings.SettingsFragmentDirections


class MainActivity : AppCompatActivity()
    {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_household, R.id.nav_targeting,
                R.id.nav_non_consenting
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setOnClickListener {
            when (it.id) {
                R.id.nav_settings -> navController.navigate(R.id.nav_settings)
                R.id.nav_profile -> navController.navigate(R.id.nav_profile)
                R.id.nav_logout -> Toast.makeText(this@MainActivity, "Logout clicked!", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}
