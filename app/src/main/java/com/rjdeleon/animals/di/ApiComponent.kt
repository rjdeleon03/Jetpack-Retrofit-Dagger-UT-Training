package com.rjdeleon.animals.di

import com.rjdeleon.animals.model.AnimalApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: AnimalApiService)
}