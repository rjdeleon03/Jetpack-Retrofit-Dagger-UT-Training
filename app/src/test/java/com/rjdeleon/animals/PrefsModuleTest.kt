package com.rjdeleon.animals

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.rjdeleon.animals.di.PrefsModule
import com.rjdeleon.animals.util.SharedPreferencesHelper

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper): PrefsModule() {

    override fun provideSharedPreferences(application: Application): SharedPreferencesHelper {
        return mockPrefs
    }

    override fun provideActivitySharedPreferences(activity: AppCompatActivity): SharedPreferencesHelper {
        return mockPrefs
    }
}