package com.wanandroid.app.logic.network.impl

import com.wanandroid.app.http.ServiceCreator
import com.wanandroid.app.logic.network.AccountService
import retrofit2.await

object AccountServiceNetwork {
    private val accountService: AccountService by lazy { ServiceCreator.create() }

    suspend fun login(username: String, password: String) =
        accountService.login(username, password).await()

    suspend fun register(username: String, password: String, rePassword: String) =
        accountService.register(username, password, rePassword).await()

    suspend fun logout() = accountService.logout().await()

    suspend fun getUserInfo() = accountService.getUserInfo().await()
}