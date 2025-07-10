package com.wanandroid.app.logic.repository

import androidx.datastore.preferences.core.edit
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.app.WanAndroidApp
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.logic.dao.SearchHistoryDataStore
import com.wanandroid.app.logic.dao.dataStore
import com.wanandroid.app.logic.network.impl.SearchNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object SearchRepository {

    // 获取datastore搜索历史记录
    val searchHistoryFlow: Flow<Set<String>> =
        WanAndroidApp.appContext.dataStore.data.map { preferences ->
            preferences[SearchHistoryDataStore.searchHistoryKey] ?: emptySet()
        }

    // 获取搜索热词
    suspend fun getSearchHotKey() = SearchNetwork.getSearchHotKey()

    // 修改datastore搜索历史记录
    suspend fun updateSearchHistory(set: Set<String>) {
        WanAndroidApp.appContext.dataStore.edit { setting ->
            setting[SearchHistoryDataStore.searchHistoryKey] = set
        }
    }

    // 获取搜索结果
    fun getSearchResults(pageSize: Int, key: String) =
        Pager(
            config = PagingConfig(pageSize = pageSize, enablePlaceholders = false),
        ) {
            IntKeyPagingSource(
                pageStart = 0,      // 搜索结果从第0页开始
                block = { page, pageSize ->
                    SearchNetwork.getSearchResults(page, pageSize, key).data?.datas ?: emptyList()
                }
            )
        }.flow

}