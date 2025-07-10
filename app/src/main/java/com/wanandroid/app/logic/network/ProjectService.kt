package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.ProjectTitle
import retrofit2.Call
import retrofit2.http.GET

interface ProjectService {

    /**
     * 获取项目分类数据
     */
    @GET("project/tree/json")
    fun getProjectTitleList(): Call<NetworkResponse<List<ProjectTitle>>>

}