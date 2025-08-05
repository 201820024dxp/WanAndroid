package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.User
import com.wanandroid.app.logic.model.UserInfo
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountService {
    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<NetworkResponse<User>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") rePassword: String
    ): Call<NetworkResponse<Any>>

    /**
     * 登出
     */
    @GET("user/logout/json")
    fun logout(): Call<NetworkResponse<Any>>

    /**
     * 获取个人信息
     */
    @GET("user/lg/userinfo/json")
    fun getUserInfo(): Call<NetworkResponse<UserInfo>>
}