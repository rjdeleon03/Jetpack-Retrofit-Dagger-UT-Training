package com.rjdeleon.animals

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rjdeleon.animals.di.AppModule
import com.rjdeleon.animals.di.DaggerViewModelComponent
import com.rjdeleon.animals.model.Animal
import com.rjdeleon.animals.model.AnimalApiService
import com.rjdeleon.animals.model.ApiKey
import com.rjdeleon.animals.util.SharedPreferencesHelper
import com.rjdeleon.animals.viewmodel.ListViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor

class ListViewModelTest {

    // Allows us to execute a task instantly and get a response
    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var animalApiService: AnimalApiService

    @Mock
    lateinit var prefs: SharedPreferencesHelper

    val application = Mockito.mock(Application::class.java)
    var listViewModel = ListViewModel(application)

    private val key = "Test Key"

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(animalApiService))
            .prefsModule(PrefsModuleTest(prefs))
            .build()
            .inject(listViewModel)
    }

    @Before
    fun setupRxSchedulers() {
        // Setup new thread and main thread
        // Runs before any test

        val immediate = object: Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run()}, true)
            }

        }

        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
    }

    @Test
    fun getAnimalsSuccess() {
        Mockito.`when`(prefs.getApiKey())
            .thenReturn(key)
        val animal = Animal("cow", null, null, null, null, null, null)
        val animalList = listOf(animal)

        val testSingle = Single.just(animalList)
        Mockito.`when`(animalApiService.getAnimals(key))
            .thenReturn(testSingle)

        listViewModel.refresh()

        Assert.assertEquals(1, listViewModel.animals.value?.size)
        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)
    }

    @Test
    fun getAnimalsFailure() {
        Mockito.`when`(prefs.getApiKey())
            .thenReturn(key)

        val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKey("OK", key))

        Mockito.`when`(animalApiService.getAnimals(key))
            .thenReturn(testSingle)
        Mockito.`when`(animalApiService.getApiKey())
            .thenReturn(keySingle)

        listViewModel.refresh()

        Assert.assertEquals(null, listViewModel.animals.value)
        Assert.assertEquals(true, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)

    }

    @Test
    fun getApiKeySuccess() {

        val testSingle = Single.just(ApiKey("", key))
        Mockito.`when`(animalApiService.getApiKey())
            .thenReturn(testSingle)

        val animal = Animal("cow", null, null, null, null, null, null)
        val animalList = listOf(animal)
        val animalTestSingle = Single.just(animalList)
        Mockito.`when`(animalApiService.getAnimals(key))
            .thenReturn(animalTestSingle)

        listViewModel.refresh()

        Assert.assertEquals(false, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)

    }

    @Test
    fun getApiKeyFailure() {

        val testSingle = Single.error<ApiKey>(Throwable())
        Mockito.`when`(animalApiService.getApiKey())
            .thenReturn(testSingle)

        val animal = Animal("cow", null, null, null, null, null, null)
        val animalList = listOf(animal)
        val animalTestSingle = Single.just(animalList)
        Mockito.`when`(animalApiService.getAnimals(key))
            .thenReturn(animalTestSingle)

        listViewModel.refresh()

        Assert.assertEquals(true, listViewModel.loadError.value)
        Assert.assertEquals(false, listViewModel.loading.value)

    }
}