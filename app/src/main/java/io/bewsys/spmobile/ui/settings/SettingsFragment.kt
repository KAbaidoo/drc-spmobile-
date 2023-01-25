package io.bewsys.spmobile.ui.settings

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import io.bewsys.spmobile.R

class SettingsFragment  :PreferenceFragmentCompat(),PreferenceFragmentCompat.OnPreferenceStartFragmentCallback{
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main,rootKey)
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        val action = SettingsFragmentDirections.actionNavSettingsToHostSettingsFragment()
        findNavController(this).navigate(action)
        return true
    }


}