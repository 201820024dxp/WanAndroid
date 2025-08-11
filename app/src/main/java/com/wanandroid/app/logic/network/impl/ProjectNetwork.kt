package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.http.catch
import com.wanandroid.app.logic.network.ProjectService
import retrofit2.await

object ProjectNetwork {

    private val projectService by lazy { ServiceCreator.create<ProjectService>() }

    suspend fun getProjectTitleList() = catch { projectService.getProjectTitleList().await() }

    suspend fun getNewestProjectList(pageNo: Int, pageSize: Int) =
        catch { projectService.getNewestProjectList(pageNo, pageSize).await() }

    suspend fun getProjectListById(pageNo: Int, categoryId: Int, pageSize: Int) =
        catch { projectService.getProjectListById(pageNo, categoryId, pageSize).await() }

}