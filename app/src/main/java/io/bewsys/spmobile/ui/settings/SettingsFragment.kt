package io.bewsys.spmobile.ui.settings

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import io.bewsys.spmobile.R

class SettingsFragment : PreferenceFragmentCompat(),
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
        if (key == "language") {
            val language = sharedPreferences.getString("language", "")

            if (language != null) {
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
                AppCompatDelegate.setApplicationLocales(appLocale)
            }
        }


    }


    override fun onStart() {
        super.onStart()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(listener)


    }

    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(listener)
    }


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main, rootKey)

        val help = preferenceManager.findPreference<Preference>("help")
        help?.setOnPreferenceClickListener {
            Log.d("Settings", "Clicked!")

            openHelpPage()

            true
        }

    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {


        val action = SettingsFragmentDirections.actionNavSettingsToHostSettingsFragment()
        findNavController(this).navigate(action)
        return true
    }

    private fun openHelpPage() {

        val url = "https://www.ey.com/en_gl/connect-with-us";
        val i =  Intent(Intent.ACTION_VIEW)

        i.data = Uri.parse(url);

        try {
            //start email intent
            startActivity(Intent.createChooser(i, "Choose Browser..."))
        }
        catch (e: Exception){

//            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        }

    }

}