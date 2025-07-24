package com.wanandroid.app.logic.model

import androidx.annotation.DrawableRes


class ProfileItemBean(
    @DrawableRes val iconResource: Int,
    val title: String = "",
    val badge: Badge = Badge()
) {
    class Badge(
        val type: BadgeType = BadgeType.NONE,
        val messageCount: Int = 0
    ) {
        enum class BadgeType {
            NONE, DOT, NUMBER
        }
    }
}