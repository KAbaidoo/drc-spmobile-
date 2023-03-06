package io.bewsys.spmobile.ui


import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.lifecycle.lifecycleScope

import androidx.navigation.NavController

import androidx.navigation.findNavController
import androidx.navigation.ui.*


import com.google.android.material.navigation.NavigationView

import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest

import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        lifecycleScope.launchWhenStarted {
            viewModel.userState.collectLatest {
                if (it.not()) {
                    navController.navigate(R.id.nav_login)
                }
            }
        }


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


        navView.setOnClickListener { menuItem ->
            when (menuItem.id) {
                R.id.nav_profile -> navController.navigate(R.id.nav_profile)
                R.id.nav_settings -> navController.navigate(R.id.nav_settings)
                R.id.nav_login -> navController.navigate(R.id.nav_login)
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


    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}
