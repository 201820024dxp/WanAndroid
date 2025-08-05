package com.wanandroid.app.logic.model

data class ShareResponse (
    val coinInfo: CoinInfo,
    val shareArticles: PageResponse<Article>
)