package com.rjdeleon.animals.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.rjdeleon.animals.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
open class PrefsModule {

    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_APP)
    open fun provideSharedPreferences(application: Application): SharedPreferencesHelper {
        return SharedPreferencesHelper(application)
    }

    @Provides
    @Singleton
    @TypeOfContext(CONTEXT_ACTIVITY)
    open fun provideActivitySharedPreferences(activity: AppCompatActivity): SharedPreferencesHelper {
        return SharedPreferencesHelper(activity)
    }
}

const val CONTEXT_APP = "CONTEXT_APP"
const val CONTEXT_ACTIVITY = "CONTEXT_ACTIVITY"

@Qualifier
annotation class TypeOfContext(val type: String)