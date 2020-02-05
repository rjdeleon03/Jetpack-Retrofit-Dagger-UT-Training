package com.rjdeleon.animals

import com.rjdeleon.animals.di.ApiModule
import com.rjdeleon.animals.model.AnimalApiService

class ApiModuleTest(val mockService: AnimalApiService): ApiModule() {

    override fun provideAnimalApiService(): AnimalApiService {
        return mockService
    }
}