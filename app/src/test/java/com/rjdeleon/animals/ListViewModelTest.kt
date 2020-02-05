package com.rjdeleon.animals

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.Executor

class ListViewModelTest {

    // Allows us to execute a task instantly and get a response
    @get:Rule
    var rule = InstantTaskExecutorRule()

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



}