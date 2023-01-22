package io.bewsys.spmobile.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.bewsys.spmobile.R

class SettingsFragment  :PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_main,rootKey)
    }

}