package com.wanandroid.app.logic.model

import androidx.annotation.DrawableRes

data class SettingItemBean(
    @DrawableRes val iconResource: Int,
    val title: String = "",
    var desc: String = "",
    val onclick: () -> Unit = {}
)
