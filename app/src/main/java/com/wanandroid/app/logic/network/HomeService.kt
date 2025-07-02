package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.model.Banner
import com.wanandroid.app.logic.model.PageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HomeService {

    /**
     * 首页banner
     */
    @GET("banner/json")
    fun getBanner(): Call<NetworkResponse<List<Banner>>>

    /**
     * 首页置顶文章
     */
    @GET("article/top/json")
    fun getArticleTopList(): Call<NetworkResponse<List<Article>>>

    /**
     * 首页文章
     */
    @GET("article/list/{pageNo}/json")
    fun getArticlePageList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): Call<NetworkResponse<PageResponse<Article>>>

}