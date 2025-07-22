package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.model.PageResponse
import com.wanandroid.app.logic.model.Chapter
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectService {

    /**
     * 获取项目分类数据
     */
    @GET("project/tree/json")
    fun getProjectTitleList(): Call<NetworkResponse<List<Chapter>>>

    /**
     * 获取最新项目列表
     */
    @GET("article/listproject/{pageNo}/json")
    fun getNewestProjectList(
        @Path("pageNo") pageNo: Int,
        @Query("page_size") pageSize: Int
    ): Call<NetworkResponse<PageResponse<Article>>>

    /**
     * 获取分类项目列表
     */
    @GET("project/list/{pageNo}/json")
    fun getProjectListById(
        @Path("pageNo") pageNo: Int,
        @Query("cid") categoryId: Int,
        @Query("page_size") pageSize: Int
    ): Call<NetworkResponse<PageResponse<Article>>>

}