package com.wanandroid.app.logic.model

data class PageResponse<T>(
    var curPage : Int,
    var datas : List<T>,
    var offset : Int,
    var over : Boolean,
    var pageCount : Int,
    var size : Int,
    var total : Int
)
