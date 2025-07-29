package com.wanandroid.app.logic.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoinInfo(
    val coinCount: Int = 0,
    val level: Int = 0,
    val nickname: String = "",
    val rank: String = "",
    val userId: Int = 0,
    val username: String = ""
) : Parcelable