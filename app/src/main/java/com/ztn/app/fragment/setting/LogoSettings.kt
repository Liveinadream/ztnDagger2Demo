package com.ztn.app.fragment.setting

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.ztn.app.R
import com.ztn.app.ui.SettingActivity


/**
 * Created by 冒险者ztn on 2019/2/20.
 * 设置界面 [SettingActivity]
 */
class LogoSettings : PreferenceFragmentCompat() {


    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.seeting)
        val pm = preferenceManager
        val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
        pm.findPreference<Preference>("boolean_value")?.summary = sp.getBoolean("boolean_value", false).toString()
    }
}