package com.wanandroid.app.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

object ServiceCreator {

    const val BASE_URL = "https://www.wanandroid.com/"

    // 创建 HttpLoggingInterceptor 实例
    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY  // 设置日志级别为 BODY，记录所有请求和响应的详细信息
    }

    // 创建 OkHttpClient 实例并添加拦截器
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)  // 添加日志拦截器
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    //    val placeService = retrofit.create(PlaceService::class.java)
    fun <T> create(serviceClass: Class<T>) : T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = retrofit.create(T::class.java)

}