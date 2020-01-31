package com.rjdeleon.animals.util

import android.content.Context
import androidx.preference.PreferenceManager

class SharedPreferencesHelper(context: Context) {

    private val PREF_API_KEY = "PREF_API_KEY"

    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun saveApiKey(key: String?) {
        mPrefs.edit().putString(PREF_API_KEY, key).apply()
    }

    fun getApiKey() = mPrefs.getString(PREF_API_KEY, null)
}