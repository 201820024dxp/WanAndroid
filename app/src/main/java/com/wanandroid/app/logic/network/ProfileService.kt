package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.ShareResponse
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

}