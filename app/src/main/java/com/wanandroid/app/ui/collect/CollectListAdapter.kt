package com.wanandroid.app.ui.collect

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.RecyclerView
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemArticleCollectedLayoutBinding
import com.wanandroid.app.logic.model.Collect
import com.wanandroid.app.logic.model.Web
import com.wanandroid.app.ui.web.WebActivity

class CollectListAdapter(diffCallback: ItemCallback<Collect>) :
    PagingDataAdapter<Collect, CollectListAdapter.ViewHolder>(diffCallback) {

    class ViewHolder(binding: ItemArticleCollectedLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.collectedTitle
        val chapter = binding.collectedChapter
        val date = binding.collectedDate
        val collect = binding.collectedIcon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(
            ItemArticleCollectedLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
        val tempContext = holder.itemView.context
        holder.itemView.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val item = getItem(position)
            if (item != null) {
                WebActivity.loadUrl(tempContext, Web.WebIntent(item.link, item.id, item.collect))
            }
        }
        holder.collect.setOnClickListener {
            val position = holder.bindingAdapterPosition
            val item = getItem(position)
            if (item != null) {
                // TODO: Handle collect/uncollect action
                item.collect = !item.collect
                Log.d(this.javaClass.simpleName, "Collect status changed: ${item.collect}")
                // Example: viewModel.collectArticle(item.id, !item.collect)
                notifyItemChanged(position, item)
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.title.text = Html.fromHtml(item.title)
            holder.chapter.text = item.chapterName
            holder.date.text = item.niceDate
            holder.collect.setImageResource(
                if (item.collect) R.drawable.ic_collect
                else R.drawable.ic_un_collect
            )
        }
    }
}

object CollectListDiffCallback : ItemCallback<Collect>() {
    override fun areItemsTheSame(oldItem: Collect, newItem: Collect): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: Collect, newItem: Collect): Boolean {
        return oldItem == newItem
    }
}