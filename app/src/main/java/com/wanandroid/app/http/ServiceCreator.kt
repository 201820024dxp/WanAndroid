package com.wanandroid.app.http

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    const val BASE_URL = "https://www.wanandroid.com/"

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //    val placeService = retrofit.create(PlaceService::class.java)
    fun <T> create(serviceClass: Class<T>) : T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = retrofit.create(T::class.java)

}