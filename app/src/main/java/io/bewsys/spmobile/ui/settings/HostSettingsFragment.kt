package io.bewsys.spmobile.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import io.bewsys.spmobile.R

class HostSettingsFragment  :PreferenceFragmentCompat(){
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.host_settings,rootKey)
    }

}