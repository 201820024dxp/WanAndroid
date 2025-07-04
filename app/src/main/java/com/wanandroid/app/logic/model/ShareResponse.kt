package com.wanandroid.app.logic.model

data class ShareResponse (
    val coinInfo: CoinInfo,
    val shareArticles: PageResponse<Article>
) {
    data class CoinInfo(
        val coinCount: Int,
        val level: Int,
        val nickname: String,
        val rank: String,
        val userId: Int,
        val username: String
    )
}