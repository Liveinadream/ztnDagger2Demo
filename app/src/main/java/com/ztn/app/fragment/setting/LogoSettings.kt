package com.ztn.app.fragment.setting

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.PreferenceManager
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
        val sp = PreferenceManager.getDefaultSharedPreferences(activity)

        pm.findPreference("boolean_value").summary = sp.getBoolean("boolean_value", false).toString()

    }
}