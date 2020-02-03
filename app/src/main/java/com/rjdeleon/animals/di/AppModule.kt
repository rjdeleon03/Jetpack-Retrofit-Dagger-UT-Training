package com.rjdeleon.animals.di

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val mApplication: Application) {

    @Provides
    fun provideApplication(): Application = mApplication
}