package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.http.catch
import com.wanandroid.app.logic.network.SearchService
import retrofit2.await

object SearchNetwork {

    private val searchService by lazy { ServiceCreator.create<SearchService>() }

    suspend fun getSearchHotKey() = catch { searchService.getSearchHotKey().await() }

    suspend fun getSearchResults(page: Int, pageSize: Int, key: String) =
        catch { searchService.getSearchResults(page, pageSize, key).await() }
}