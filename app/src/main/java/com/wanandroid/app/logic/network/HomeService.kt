package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.BannerResponse
import retrofit2.Call
import retrofit2.http.GET

interface HomeService {

    @GET("banner/json")
    fun getBanner(): Call<BannerResponse>
}