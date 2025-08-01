package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Collect
import com.wanandroid.app.logic.model.PageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CollectService {

    /**
     * 获取收藏文章列表
     */
    @GET("lg/collect/list/{pageNo}/json")
    fun getCollectList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int = 20
    ) : Call<NetworkResponse<PageResponse<Collect>>>

    /**
     * 收藏站内文章
     */
    @POST("lg/collect/{id}/json")
    fun collectArticleById(
        @Path("id") id: Int
    ) : Call<NetworkResponse<Any>>

    /**
     * 取消收藏站内文章
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun unCollectArticleById(
        @Path("id") id: Int
    ) : Call<NetworkResponse<Any>>
}