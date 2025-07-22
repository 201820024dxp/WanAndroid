package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.model.Chapter
import com.wanandroid.app.logic.model.PageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {

    /**
     * 获取公众号列表
     */
    @GET("wxarticle/chapters/json")
    fun getGroupChapterList() : Call<NetworkResponse<List<Chapter>>>

    /**
     * 获取公众号文章
     */
    @GET("wxarticle/list/{id}/{page}/json")
    fun getGroupArticleListById(
        @Path("id") id: Int,
        @Path("page") page: Int,
        @Query("page_size") pageSize: Int = 20
    ) : Call<NetworkResponse<PageResponse<Article>>>

}