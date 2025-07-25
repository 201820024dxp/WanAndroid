package com.wanandroid.app.utils

import android.widget.Toast
import com.wanandroid.app.app.WanAndroidApp

fun String?.showShortToast() =
    if (!this.isNullOrBlank()) {
        Toast.makeText(WanAndroidApp.appContext, this, Toast.LENGTH_SHORT).show()
    } else Unit

fun String?.showLongToast() =
    if (!this.isNullOrBlank()) {
        Toast.makeText(WanAndroidApp.appContext, this, Toast.LENGTH_LONG).show()
    } else Unit