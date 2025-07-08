package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.model.HotKey
import com.wanandroid.app.logic.model.PageResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {

    /**
     * 热搜词
     */
    @GET("hotkey/json")
    fun getSearchHotKey(): Call<NetworkResponse<List<HotKey>>>

    /**
     * 搜索结果
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    fun getSearchResults(
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Field("k") key: String
    ): Call<NetworkResponse<PageResponse<Article>>>
}