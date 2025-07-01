package com.wanandroid.app.logic.repository

import com.wanandroid.app.logic.network.impl.HomeServiceNetwork

object HomeRepository {

    suspend fun getBanner()  = HomeServiceNetwork.getBanner()

}