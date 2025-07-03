package com.wanandroid.app.ui.home.item

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemHomeArticleLayoutBinding
import com.wanandroid.app.logic.model.Article

class HomeArticleAdapter(diffCallback: DiffUtil.ItemCallback<Article>) :
    PagingDataAdapter<Article, HomeArticleAdapter.ViewHolder>(diffCallback) {

    class ViewHolder(binding: ItemHomeArticleLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val clItem = binding.clItem
        val tvAuthor = binding.tvAuthor
        val tvIsTop = binding.tvIsTop
        val tvNew = binding.tvNew
        val tvTag1 = binding.tvTag1
        val tvTag2 = binding.tvTag2
        val tvDate = binding.tvDate
        val tvContent = binding.tvContent
        val tvType2 = binding.tvType2
        val ivCollect = binding.ivCollect
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeArticleLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        // initialize views
        holder.tvAuthor.text = item?.shareUser ?: "unknown"
        holder.tvIsTop.visibility = if (item?.type == 1) View.VISIBLE else View.GONE    // Warn: API没有置顶文章，尚不清楚“置顶”的显示逻辑，现在的代码不一定正确
        holder.tvNew.visibility = if (item?.fresh == true) View.VISIBLE else View.GONE
        item?.tags?.let {
            if (it.size == 1) {
                holder.tvTag1.visibility = View.VISIBLE
                holder.tvTag1.text = it[0].name
            } else if (it.size >= 2) {
                holder.tvTag1.visibility = View.VISIBLE
                holder.tvTag1.text = it[0].name
                holder.tvTag2.visibility = View.VISIBLE
                holder.tvTag2.text = it[1].name
            } else {
                holder.tvTag1.visibility = View.GONE
                holder.tvTag2.visibility = View.GONE
            }
        }
        holder.tvDate.text = item?.niceDate ?: "unknown date"
        holder.tvContent.text = item?.title ?: "No title"
        holder.tvType2.visibility = if (item?.superChapterName.isNullOrEmpty()) View.GONE else View.VISIBLE
        holder.tvType2.text = item?.let{"${it.superChapterName}>${it.chapterName}"} ?: ""
        holder.ivCollect.setImageResource(
            if (item?.collect == true) R.drawable.ic_collect    // 收藏状态
            else R.drawable.ic_un_collect                       // 未收藏状态
        )

        // initialize events
        holder.clItem.setOnClickListener {
            item?.let { article ->
                // TODO: item点击事件
                Log.d("HomeArticleAdapter", "Item clicked: ${article.title}")
            }
        }
        holder.tvAuthor.setOnClickListener {
            item?.let { article ->
                // TODO: 作者点击事件
                Log.d("HomeArticleAdapter", "Author clicked: ${article.shareUser}")
            }
        }
        holder.ivCollect.setOnClickListener {
            item?.let { article ->
                // TODO: 收藏点击事件
                Log.d("HomeArticleAdapter", "Collect clicked: ${article.title}")
            }
        }
    }
}


object HomeArticleDiffCallback : DiffUtil.ItemCallback<Article>() {

    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        // Implement your logic to check if items are the same
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        // Implement your logic to check if contents of items are the same
        return oldItem == newItem
    }
}
