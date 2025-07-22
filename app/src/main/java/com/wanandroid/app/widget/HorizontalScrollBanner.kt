package com.wanandroid.app.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.Banner
import com.youth.banner.adapter.BannerAdapter
import kotlin.math.abs

class HorizontalScrollBanner<T, BA: BannerAdapter<T, RecyclerView.ViewHolder>>(
    context: Context, attributeSet: AttributeSet?, defStyleAttr: Int
) : Banner<T, BA>(context, attributeSet, defStyleAttr) {

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context) : this(context, null)

    // 滑动距离范围
    private var mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop / 2
    // 记录触摸的位置（主要用于解决事件冲突问题）
    private var mStartX = 0f
    private var mStartY = 0f
    // 记录viewpager2是否被拖动
    private var mIsViewPager2Drag = false
    // 是否要拦截事件
    private var isIntercept = true
    // 记录滑动方向
    private var mDirection = 0

    // 设置轮播图横向滚动
    override fun setOrientation(orientation: Int): Banner<*, out BannerAdapter<*, *>> {
        return super.setOrientation(HORIZONTAL)
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        if (!viewPager2.isUserInputEnabled || !isIntercept) {
            return super.onInterceptTouchEvent(event)
        }
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.x
                mStartY = event.y
            }

            MotionEvent.ACTION_MOVE -> {
                val endX = event.x
                val endY = event.y
                val distanceX = abs((endX - mStartX).toDouble()).toFloat()
                val distanceY = abs((endY - mStartY).toDouble()).toFloat()
                mIsViewPager2Drag = distanceX > mTouchSlop && distanceX > distanceY
                Log.d("HorizontalScrollBanner", "$mIsViewPager2Drag")
                parent.requestDisallowInterceptTouchEvent(mIsViewPager2Drag)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL ->
                parent.requestDisallowInterceptTouchEvent(false)
        }
        return false
    }
}