package com.wanandroid.app.ui.project.child

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentProjectChildBinding
import com.wanandroid.app.logic.model.ProjectTitle

class ProjectChildFragment :BaseFragment<FragmentProjectChildBinding>() {

    companion object {
        const val ARG_TITLE = "arg_title"
        const val PROJECT_ID_NEWEST = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProjectChildBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf { it.containsKey(ARG_TITLE) }?.apply {
            val projectTitle = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                getParcelable(ARG_TITLE, ProjectTitle::class.java)
            } else {
                getParcelable(ARG_TITLE)
            }
            binding.projectTextView.text = projectTitle?.name.toString()
        }
    }

}