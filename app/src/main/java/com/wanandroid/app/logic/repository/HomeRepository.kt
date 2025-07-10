package com.wanandroid.app.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.http.NetworkResponse
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.model.PageResponse
import com.wanandroid.app.logic.network.impl.HomeServiceNetwork
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

object HomeRepository {

    suspend fun getBanner() = HomeServiceNetwork.getBanner()

    suspend fun getArticleTopList() = HomeServiceNetwork.getArticleTopList()

    fun getArticlePageList(pageSize: Int) =
        Pager(
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                pageStart = 0,      // 首页文章列表从第0页开始
                block = { page, pageSize ->
                    val response = ArrayList<Article>()
                    supervisorScope {
                        // 使用async并行请求
                        val topsDeferred = async { getArticleTopList() }
                        val articlesDeferred =
                            async { HomeServiceNetwork.getArticlePageList(page, pageSize) }

                        // 获取置顶文章和普通文章
                        val tops = topsDeferred.await().data ?: emptyList()
                        val articles = articlesDeferred.await().data?.datas ?: emptyList()

                        response.addAll(tops)
                        response.addAll(articles)
                    }
                    response
                }
            )
        }.flow      // 返回一个Flow对象

    fun getSquareArticlePageList(pageSize: Int) =
        Pager(
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                pageStart = 0,      // 广场文章列表从第0页开始
                block = { page, pageSize ->
                    // 获取广场文章
                    HomeServiceNetwork.getSquareArticlePageList(page, pageSize).data?.datas
                        ?: emptyList()
                }
            )
        }.flow

    fun getAnswerPageList(pageSize: Int) =
        Pager(
            PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                pageStart = 1,      // 问答列表从第1页开始
                block = { page, pageSize ->
                    // 获取问答文章
                    HomeServiceNetwork.getAnswerPageList(page, pageSize).data?.datas
                        ?: emptyList()
                }
            )
        }.flow

}