package com.m4up.accorchestra.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.m4up.accorchestra.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}