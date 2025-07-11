package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.network.NavigationService
import retrofit2.await

object NavigationServiceNetwork {

    private val navigationService by lazy { ServiceCreator.create<NavigationService>() }

    suspend fun getNavigationList() = navigationService.getNavigationList().await()

}