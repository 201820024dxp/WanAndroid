package com.wanandroid.app.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.logic.model.Navigation
import com.wanandroid.app.logic.network.impl.NavigationServiceNetwork
import com.wanandroid.app.logic.model.BusinessMode

object NavigationRepository {

    /**
     * 获取导航列表
     */
    suspend fun getNavigationList(): List<Navigation> {
        val navigationList =
            (NavigationServiceNetwork.getNavigationList().data ?: emptyList()).toMutableList()
        val iterator = navigationList.iterator()
        // 如果导航文章列表为空，则从navigationList中移除该条记录
        while (iterator.hasNext()) {
            val navigation = iterator.next()
            if (navigation.articles.isEmpty()) {
                iterator.remove()
            }
        }
        return navigationList.toList()
    }

    /**
     * 获取体系目录列表
     */
    suspend fun getSystemChapterList() =
        NavigationServiceNetwork.getSystemChapterList().data ?: emptyList()

    /**
     * 获取体系文章列表
     */
    fun getSystemArticleList(cid: Int, pageSize: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                pageStart = 0,  // 体系文章列表从第0页开始
                block = { page, size ->
                    NavigationServiceNetwork.getSystemArticleList(page, cid, size).data?.datas
                        ?: emptyList()
                }
            )
        }.flow

    /**
     * 获取教程目录列表，复用ProjectTitle实体
     */
    suspend fun getCourseChapterList() =
        NavigationServiceNetwork.getCourseChapterList().data ?: emptyList()


    /**
     * 获取教程文章列表
     */
    fun getCourseListById(cid: Int, orderType: Int = 1, pageSize: Int = 20) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                pageStart = 0,  // 教程文章列表从第0页开始
                block = { page, size ->
                    val articleList =
                        (NavigationServiceNetwork.getCourseListById(page, cid, orderType, size)
                            .data?.datas ?: emptyList()).toMutableList()
                    articleList.forEach { article ->
                        article.buzMode = BusinessMode.COURSE
                    }
                    articleList.toList()
                }
            )
        }.flow
}