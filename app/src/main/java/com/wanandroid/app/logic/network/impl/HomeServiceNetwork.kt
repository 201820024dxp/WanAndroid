package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.network.HomeService
import retrofit2.await

object HomeServiceNetwork {

    private val homeService by lazy { ServiceCreator.create<HomeService>() }

    suspend fun getBanner() = homeService.getBanner().await()

}