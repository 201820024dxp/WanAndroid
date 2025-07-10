package com.wanandroid.app.http

/**
 * wanandroid所有接口返回值都是errorCode，errorMsg，data的格式
 * 为了简化model的编写，将errorCode，errorMsg提取出来，model层仅需关注具体的data类型即可
 */
data class NetworkResponse<T> (
    var errorCode: Int = 0,
    var errorMsg: String = "",
    var data: T? = null
)