package com.wanandroid.app.ui.navigation.child.navigation

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.wanandroid.app.databinding.ItemNavigatorChildTagLayoutBinding
import com.wanandroid.app.databinding.ItemTextViewChipBinding
import com.wanandroid.app.logic.model.Navigation
import com.wanandroid.app.ui.web.WebActivity

class NavigationChildAdapter(var navigationList: List<Navigation>) :
    RecyclerView.Adapter<NavigationChildAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemNavigatorChildTagLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val item = binding.navChildTagItem
        val navChildTagTitle = binding.navChildTagTitle
        val navChildTagFlexbox = binding.navChildTagFlexbox
    }

    override fun getItemCount(): Int = navigationList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNavigatorChildTagLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val navigation = navigationList[position]
        if (navigation.articles.isNotEmpty()) {
            holder.navChildTagTitle.text = navigation.name
            holder.navChildTagFlexbox.removeAllViews()
            // Populate the FlexboxLayout with tags for each article in the navigation
            navigation.articles.forEach { article ->
                val tagView =
                    ItemTextViewChipBinding.inflate(LayoutInflater.from(holder.itemView.context))
                tagView.chipTextView.text = Html.fromHtml(article.title)
                tagView.chipLayout.setOnClickListener {
                    // 设置点击事件
                    Log.d("NavigationChildAdapter", "Clicked on tag: ${article.title}")
                    WebActivity.loadUrl(holder.itemView.context, article.link)
                }
                // item_text_view_chip.xml 的 root view 定义的 margin 不起作用，手动设置 chip margin
                val params = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(12, 12, 12, 12) // 设置你需要的 margin
                tagView.root.layoutParams = params
                holder.navChildTagFlexbox.addView(tagView.root)
            }
        } else {
            // 如果没有文章，隐藏该条目
            holder.itemView.visibility = ViewGroup.GONE
            holder.itemView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                0 // 高度设置为0
            )
        }
    }

}