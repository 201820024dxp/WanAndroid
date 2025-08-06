package com.wanandroid.app.ui.navigation.course

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wanandroid.app.R
import com.wanandroid.app.databinding.ItemProjectArticleLayoutBinding
import com.wanandroid.app.logic.model.Chapter
import com.wanandroid.app.ui.navigation.course.child.CourseListActivity

class CourseChildAdapter(var chapterList: List<Chapter>) :
    RecyclerView.Adapter<CourseChildAdapter.ViewHolder>() {

    // 复用 Project 的列表 item
    class ViewHolder(binding: ItemProjectArticleLayoutBinding)
        : RecyclerView.ViewHolder(binding.root) {
        val cCardView = binding.projectItem
        val cImage = binding.projectItemImageView
        val cTitle = binding.projectItemTitle
        val cContent = binding.projectItemContent
        val cDate = binding.projectItemDate
        val cAuthor = binding.projectItemAuthor
        val cCollect = binding.projectItemCollect

        init {
            cAuthor.visibility = View.GONE
            cCollect.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProjectArticleLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        val viewHolder = ViewHolder(binding)
        // 新建view时设置一次点击事件就行，不必为每个item创建新的OnClickListener实例
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val chapter = chapterList[position]
            Log.d("CourseChildAdapter",
                "you click course ${chapter.name}, with cid=${chapter.id}")
            // 获取临时上下文，这里指的是ViewGroup（教程栏目的ConstraintLayout）的上下文
            val tempContext = parent.context
            val intent = Intent(tempContext, CourseListActivity::class.java)
            intent.putExtra(CourseListActivity.KEY_COURSE_LIST_BUNDLE, chapter)
            tempContext.startActivity(intent)
        }
        return viewHolder
    }

    override fun getItemCount(): Int = chapterList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chapter = chapterList[position]
        Glide.with(holder.itemView)
            .load(chapter.cover)
            .placeholder(R.drawable.default_project_img)
            .into(holder.cImage)
        holder.cTitle.text = chapter.name.split("_")[0]     // 取消标题中下划线之后的作者名
        holder.cContent.text = chapter.desc
        holder.cDate.text = chapter.author
    }
}