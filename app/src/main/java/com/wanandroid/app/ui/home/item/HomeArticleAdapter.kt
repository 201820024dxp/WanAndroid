package com.wanandroid.app.ui.home.item

import android.content.Context
import android.content.Intent
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemHomeArticleLayoutBinding
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.model.Web
import com.wanandroid.app.ui.home.child.explore.ExploreFragment
import com.wanandroid.app.ui.share.ShareListActivity
import com.wanandroid.app.ui.web.WebActivity
import com.wanandroid.app.widget.BusinessMode

class HomeArticleAdapter(val context: Context, diffCallback: DiffUtil.ItemCallback<Article>) :
    PagingDataAdapter<Article, HomeArticleAdapter.ViewHolder>(diffCallback) {

    class ViewHolder(binding: ItemHomeArticleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
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
        holder.tvAuthor.text = item?.let { if (it.shareUser != "") it.shareUser else it.author }
        holder.tvIsTop.visibility =
            if (item?.type == 1) View.VISIBLE else View.GONE
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
        holder.tvContent.text = Html.fromHtml(item?.title).toString() ?: "No title"
        holder.tvType2.visibility =
            if (item?.superChapterName.isNullOrEmpty()) View.GONE else View.VISIBLE
        holder.tvType2.text = item?.let { "${it.superChapterName} > ${it.chapterName}" } ?: ""
        holder.ivCollect.setImageResource(
            if (item?.collect == true) R.drawable.ic_collect    // 收藏状态
            else R.drawable.ic_un_collect                       // 未收藏状态
        )

        when (item?.buzMode) {
            BusinessMode.NORM -> {
                holder.ivCollect.isVisible = true
            }

            BusinessMode.COURSE -> {
                holder.ivCollect.isVisible = false
            }

            else -> {
                holder.ivCollect.isVisible = true
            }
        }

        // initialize events
        holder.clItem.setOnClickListener {
            item?.let { article ->
                // TODO: item点击事件
                Log.d("HomeArticleAdapter", "Item clicked: ${article.title}")

                when (article.buzMode) {
                    BusinessMode.NORM -> {
                        WebActivity.loadUrl(
                            context,
                            Web.WebIntent(
                                url = article.link,
                                id = article.id,
                                collect = article.collect
                            )
                        )
                    }

                    BusinessMode.COURSE -> {
                        WebActivity.loadUrl(context, article.link)
                    }
                }

            }
        }

        holder.tvAuthor.setOnClickListener {
            item?.let { article ->
                // TODO: 作者点击事件
                Log.d("HomeArticleAdapter", "Author clicked: ${article.shareUser}")
                // 方法一：根据当前上下文实现不同的跳转逻辑
//                when (context) {
//                    !is ShareListActivity -> {
//                        // 如果不是在分享列表页面，则跳转到分享列表页面
//                        val intent = Intent(this.context, ShareListActivity::class.java)
//                        intent.putExtra(ShareListActivity.KEY_SHARE_LIST_USER_ID, article.userId.toString())
//                        context.startActivity(intent)
//                    }
//                    else -> {
//                        // 在分享列表页面则不需要跳转
//                        Log.d("HomeArticleAdapter", "Already in ShareListActivity")
//                    }
//                }

                // 方法二：设置启动模式为SingleTop
                if (article.shareUser != "") { // shareUser为空代表该文章非分享文章（站内文章），不应该跳转到分享列表
                    val intent = Intent(context, ShareListActivity::class.java).apply {
                        putExtra(
                            ShareListActivity.KEY_SHARE_LIST_USER_ID,
                            article.userId.toString()
                        )
                        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP // 设置启动模式为SingleTop
                    }
                    context.startActivity(intent)
                }
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
