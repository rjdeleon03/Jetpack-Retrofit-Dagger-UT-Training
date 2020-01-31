package com.rjdeleon.animals.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.rjdeleon.animals.model.Animal
import com.rjdeleon.animals.model.AnimalApiService
import com.rjdeleon.animals.model.ApiKey
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

    override fun onCleared() {
        super.onCleared()
        mDisposable.clear()
    }

    fun refresh() {
        startRefreshing()
        getKey()
    }

    private fun startRefreshing() {
        loadError.value = false
        loading.value = true
    }

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
                            getAnimals(apiKey.key)
                        }

                    }

                    override fun onError(e: Throwable) {
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