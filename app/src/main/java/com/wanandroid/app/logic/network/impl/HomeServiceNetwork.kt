package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.network.HomeService
import retrofit2.await

object HomeServiceNetwork {

    private val homeService by lazy { ServiceCreator.create<HomeService>() }

    suspend fun getBanner() = homeService.getBanner().await()

    suspend fun getArticleTopList() = homeService.getArticleTopList().await()

    suspend fun getArticlePageList(pageNo: Int, pageSize: Int) =
        homeService.getArticlePageList(pageNo, pageSize).await()

    suspend fun getSquareArticlePageList(pageNo: Int, pageSize: Int) =
        homeService.getSquareArticlePageList(pageNo, pageSize).await()

}