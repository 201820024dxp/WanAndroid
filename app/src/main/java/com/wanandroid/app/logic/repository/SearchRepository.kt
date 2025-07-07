package com.wanandroid.app.logic.repository

import com.wanandroid.app.logic.network.impl.SearchNetwork

object SearchRepository {

    suspend fun getSearchHotKey() = SearchNetwork.getSearchHotKey()

}