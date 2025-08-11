package com.wanandroid.app.ui.project.child

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemProjectArticleLayoutBinding
import com.wanandroid.app.eventbus.FlowBus
import com.wanandroid.app.logic.model.Article
import com.wanandroid.app.logic.model.Web
import com.wanandroid.app.logic.repository.CollectRepository
import com.wanandroid.app.ui.account.AccountManager
import com.wanandroid.app.ui.web.WebActivity
import com.wanandroid.app.utils.showShortToast

class ProjectArticleAdapter(diffCallback: DiffUtil.ItemCallback<Article>) :
    PagingDataAdapter<Article, ProjectArticleAdapter.ViewHolder>(diffCallback) {

    class ViewHolder(binding: ItemProjectArticleLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val item = binding.projectItem
        val image = binding.projectItemImageView
        val title = binding.projectItemTitle
        val content = binding.projectItemContent
        val date = binding.projectItemDate
        val author = binding.projectItemAuthor
        val collectIcon = binding.projectItemCollect
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProjectArticleLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val holder = ViewHolder(binding)
        // Set click listener
        holder.item.setOnClickListener {    // item click listener
            val position = holder.bindingAdapterPosition
            val item = getItem(position)
            item?.let { article ->
                WebActivity.loadUrl(
                    it.context,
                    Web.WebIntent(
                        url = article.link,
                        id = article.id,
                        collect = article.collect
                    )
                )
            }
        }

        holder.collectIcon.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val item = getItem(position)
            // Handle collect/unCollect action
            Log.d(
                "ProjectArticleAdapter",
                "Project collectIcon clicked for article: ${item?.title}"
            )
            val context = holder.itemView.context
            AccountManager.checkLogin(context) {
                if (item != null) {
                    CollectRepository.changeArticleCollectStateById(item.id, item.collect)
                        .observeForever {
                            when (it?.errorCode) {
                                0 -> {
                                    item.collect = !item.collect
                                    notifyItemChanged(position, item)
                                    // 发送收藏状态的改变
                                    FlowBus.collectStateFlow.tryEmit(
                                        FlowBus.CollectStateChangedItem(
                                            item.id, item.collect
                                        )
                                    )
                                }

                                else -> {
                                    it?.errorMsg.showShortToast()
                                }
                            }
                        }
                }
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position) ?: return  // 对item进行空检查，只有在非空时才执行相关操作
        // Initialize views
        Glide.with(holder.itemView.rootView.context)
            .load(item.envelopePic)
            .placeholder(R.drawable.default_project_img)
            .into(holder.image)
        holder.title.text = item.title ?: ""
        holder.content.text = item.desc ?: ""
        holder.date.text = item.niceDate ?: ""
        holder.author.text = item.author ?: "未知作者"
        // Set collect icon visibility based on whether the article is collected
        holder.collectIcon.setImageResource(
            if (item.collect) R.drawable.ic_collect
            else R.drawable.ic_un_collect
        )
    }

}