package com.wanandroid.app.logic.network

import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.ShareResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileService {

    @GET("user/{userId}/share_articles/{page}/json")
    fun getUserShareList(
        @Path("userId") userId: String,
        @Path("page") page: Int
    ) : Call<NetworkResponse<ShareResponse>>

}