package com.wanandroid.app.logic.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.wanandroid.app.base.IntKeyPagingSource
import com.wanandroid.app.logic.network.impl.ProjectNetwork

object ProjectRepository {

    // 获取项目分类数据
    suspend fun getProjectTitleList() = ProjectNetwork.getProjectTitleList()?.data ?: emptyList()

    // 获取最新项目列表
    fun getNewestProjectList(pageSize: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                pageStart = 0,  // 最新项目列表从第0页开始
                block = { page, pageSize ->
                    ProjectNetwork.getNewestProjectList(page, pageSize)?.data?.datas ?: emptyList()
                }
            )
        }.flow

    // 获取分类项目列表
    fun getProjectListById(categoryId: Int, pageSize: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                enablePlaceholders = false
            )
        ) {
            IntKeyPagingSource(
                pageStart = 1,  // 分类项目列表从第1页开始
                block = { page, size ->
                    ProjectNetwork.getProjectListById(page, categoryId, size)?.data?.datas
                        ?: emptyList()
                }
            )
        }.flow

}