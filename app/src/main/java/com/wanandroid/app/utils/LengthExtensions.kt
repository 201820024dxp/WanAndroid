package com.wanandroid.app.utils

import android.content.res.Resources

inline val Int.dp
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()