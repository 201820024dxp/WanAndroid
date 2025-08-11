package com.wanandroid.app.http

import android.util.Log

/**
 * wanandroid所有接口返回值都是errorCode，errorMsg，data的格式
 */
data class NetworkResponse<T> (
    var errorCode: Int = 0,
    var errorMsg: String = "",
    var data: T? = null
)

inline fun <T> catch(call:() -> T) = try {
    call()
} catch (e: Exception) {
    Log.e("NetworkResponse", e.stackTraceToString())
    null
}

fun <T> NetworkResponse<T>.getOrNull() = try {
    this.data
} catch (e: Exception) {
    Log.e("NetworkResponse", e.stackTraceToString())
    null
}