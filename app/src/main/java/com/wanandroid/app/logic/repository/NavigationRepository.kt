package com.wanandroid.app.logic.repository

import com.wanandroid.app.logic.network.impl.NavigationServiceNetwork

object NavigationRepository {

    suspend fun getNavigationList() = NavigationServiceNetwork.getNavigationList().data ?: emptyList()

}