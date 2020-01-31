package com.rjdeleon.animals.model

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnimalApi {

    /*
    Single is an observable that can be observed by another entity
    Whenever observed, we will get either one response or error
    Used for single response values
     */
    @GET("getKey")
    fun getApiKey(): Single<ApiKey>

    @POST("getAnimals")
    @FormUrlEncoded
    fun getAnimals(@Field("key") key: String): Single<List<Animal>>
}