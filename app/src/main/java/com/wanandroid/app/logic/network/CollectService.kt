package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Collect
import com.wanandroid.app.logic.model.PageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CollectService {

    @GET("lg/collect/list/{pageNo}/json")
    fun getCollectList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int = 20
    ) : Call<NetworkResponse<PageResponse<Collect>>>
}