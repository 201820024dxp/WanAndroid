package com.wanandroid.app.logic.model

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class Web(val className: String, val bundle: Bundle) {

    @Parcelize
    data class WebIntent(
        val url: String,
        val id: Int = 0,
        var collect: Boolean = false
    ) : Parcelable {
        fun isNeedShowCollectIcon(): Boolean {
            return id != 0
        }
    }

    companion object {
            const val KEY_WEB_VIEW_Intent_bundle = "key_web_view_intent_bundle"
        }
}
