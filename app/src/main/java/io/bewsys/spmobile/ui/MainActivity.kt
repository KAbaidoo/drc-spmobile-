package io.bewsys.spmobile.ui


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*

import com.google.android.material.navigation.NavigationView
import io.bewsys.spmobile.BuildConfig

import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.ActivityMainBinding

import kotlinx.coroutines.flow.collectLatest

import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity(), UIController {


    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    //============================

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable
    private var mTime: Long = BuildConfig.SESSION_TIMEOUT


    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Initializing the handler and the runnable
        mHandler = Handler(Looper.getMainLooper())
//
        mRunnable = Runnable {

//            navController.navigate(R.id.nav_login)

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


       //get user auth state
        lifecycleScope.launchWhenStarted {
            viewModel.userState.collectLatest {
                if (it.not()) {
//                    navController.navigate(R.id.nav_login)
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

    override fun showProgressBar(show: Boolean) {
        binding.appBarMain.progressBarMain.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

}
