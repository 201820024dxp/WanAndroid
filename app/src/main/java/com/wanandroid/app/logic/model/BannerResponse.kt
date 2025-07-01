package com.wanandroid.app.logic.model

data class BannerResponse(
    var errorCode: Int = 0,
    var errorMsg: String = "",
    var data: List<Banner> = emptyList()
) {
    data class Banner(
        var desc: String = "",
        var id: Int = 0,
        var imagePath: String = "",
        var isVisible: Int = 0,
        var order: Int = 0,
        var title: String = "",
        var type: Int = 0,
        var url: String = ""
    )
}