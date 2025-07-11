package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Navigation
import retrofit2.Call
import retrofit2.http.GET

interface NavigationService {

    /**
     * 获取导航数据
     */
    @GET("navi/json")
    fun getNavigationList(): Call<NetworkResponse<List<Navigation>>>
}