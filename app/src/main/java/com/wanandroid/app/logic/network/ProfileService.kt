package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.ShareResponse
import com.wanandroid.app.logic.model.Tool
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProfileService {

    /**
     * 获取用户分享文章列表
     */
    @GET("user/{userId}/share_articles/{page}/json")
    fun getUserShareList(
        @Path("userId") userId: String,
        @Path("page") page: Int
    ) : Call<NetworkResponse<ShareResponse>>

    /**
     * 分享文章
     */
    @FormUrlEncoded
    @POST("lg/user_article/add/json")
    fun postShareArticle(
        @Field("title") title: String,
        @Field("link") link: String
    ) : Call<NetworkResponse<Any>>

    /**
     * 删除分享文章
     */
    @POST("lg/user_article/delete/{id}/json")
    fun deleteShareArticle(
        @Path("id") id: Int
    ) : Call<NetworkResponse<Any>>

    /**
     * 工具列表
     */
    @GET("tools/list/json")
    fun getToolList(): Call<NetworkResponse<List<Tool>>>

}