package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.network.CollectService
import retrofit2.await

object CollectServiceNetwork {

    private val collectService: CollectService by lazy { ServiceCreator.create() }

    suspend fun getCollectList(pageNo: Int, pageSize: Int = 20) =
        collectService.getCollectList(pageNo, pageSize).await()
}