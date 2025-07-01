package com.wanandroid.app.ui.home.item

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.wanandroid.app.logic.model.BannerResponse.Banner
import com.youth.banner.adapter.BannerAdapter

class HomeBannerAdapter(items: List<Banner>) :
    BannerAdapter<Banner, HomeBannerAdapter.BannerViewHolder>(items) {


    class BannerViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val imageView = ImageView(parent.context)
        // viewpage2强制要求设置为match_parent
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // 设置图片缩放类型为“居中裁剪”
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

        val holder = BannerViewHolder(imageView)
        return holder
    }

    override fun onBindView(holder: BannerViewHolder, data: Banner,
                            position: Int, size: Int) {
        Glide.with(holder.itemView)
            .load(data.imagePath)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
            .into(holder.imageView)
    }

}