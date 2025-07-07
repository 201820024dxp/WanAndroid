package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.HotKey
import retrofit2.Call
import retrofit2.http.GET

interface SearchService {

    /**
     * 热搜词
     */
    @GET("hotkey/json")
    fun getSearchHotKey(): Call<NetworkResponse<List<HotKey>>>

}