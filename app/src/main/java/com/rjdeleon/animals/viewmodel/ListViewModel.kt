package com.rjdeleon.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rjdeleon.animals.model.Animal
import com.rjdeleon.animals.model.AnimalApiService
import com.rjdeleon.animals.model.ApiKey
import com.rjdeleon.animals.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class ListViewModel(application: Application): AndroidViewModel(application) {

    // Lazy - instantiated only when it is needed
    val animals by lazy { MutableLiveData<List<Animal>>() }
    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }

    private val mDisposable = CompositeDisposable()
    private val mApiService = AnimalApiService()
    private val mPrefs = SharedPreferencesHelper(application)

    private var mInvalidApiKey = false

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }

    fun refresh() {
        startRefreshing()
        val key = getKeyFromSharedPrefs()
        if (key == null) {
            getKey()
        } else {
            getAnimals(key)
        }
    }

    private fun startRefreshing() {
        mInvalidApiKey = false
        loadError.value = false
        loading.value = true
    }

    private fun getKeyFromSharedPrefs() =
        mPrefs.getApiKey()

    private fun getKey() {
        // Create a disposable and dismiss when VM is destroyed
        mDisposable.add(
            mApiService.getApiKey()
                .subscribeOn(Schedulers.newThread()) // Operation is performed in the background
                .observeOn(AndroidSchedulers.mainThread()) // Post action is done in main thread
                .subscribeWith(object: DisposableSingleObserver<ApiKey>() {

                    override fun onSuccess(apiKey: ApiKey) {

                        if (apiKey.key.isNullOrBlank()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            mPrefs.saveApiKey(apiKey.key)
                            getAnimals(apiKey.key)
                        }

                    }

                    override fun onError(e: Throwable) {
                        if (!mInvalidApiKey) {
                            mInvalidApiKey = true
                            getKey()
                            return
                        }
                        e.printStackTrace()
                        loading.value = false
                        loadError.value = true
                    }
                })
        )

    }

    private fun getAnimals(key: String) {
        mDisposable.add(
            mApiService.getAnimals(key)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableSingleObserver<List<Animal>>() {
                    override fun onSuccess(animalList: List<Animal>) {
                        loadError.value = false
                        animals.value = animalList
                        loading.value = false
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        loading.value = false
                        loadError.value = true
                    }
                })
        )
    }

}