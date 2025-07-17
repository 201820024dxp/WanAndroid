package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.model.Navigation
import com.wanandroid.app.logic.model.PageResponse
import com.wanandroid.app.logic.model.SystemTopDirectory
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NavigationService {

    /**
     * 获取导航数据
     */
    @GET("navi/json")
    fun getNavigationList(): Call<NetworkResponse<List<Navigation>>>

    /**
     * 获取体系目录数据
     */
    @GET("tree/json")
    fun getSystemChapterList(): Call<NetworkResponse<List<SystemTopDirectory>>>

    /**
     * 获取体系文章列表
     */
    @GET("article/list/{pageNo}/json")
    fun getSystemArticleList(
        @Path("pageNo") pageNo: Int,
        @Query("cid") cid: Int,
        @Query("page_size") pageSize: Int
    ): Call<NetworkResponse<PageResponse<Article>>>
}