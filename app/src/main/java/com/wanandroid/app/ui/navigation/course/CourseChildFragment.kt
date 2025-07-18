package com.wanandroid.app.ui.navigation.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentNavigatorChildCourseBinding

class CourseChildFragment : BaseFragment<FragmentNavigatorChildCourseBinding>() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var courseAdapter: CourseChildAdapter
    private val viewModel : CourseChildViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNavigatorChildCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize your views and data here
        initView()
        initEvents()
    }

    private fun initView() {
        courseAdapter = CourseChildAdapter(emptyList())
        linearLayoutManager = LinearLayoutManager(this.requireContext())
        binding.courseChapterRecyclerView.apply {
            adapter = courseAdapter
            layoutManager = linearLayoutManager
            setHasFixedSize(true)
        }
    }

    private fun initEvents() {
        viewModel.courseChapterList.observe(viewLifecycleOwner) { chapters ->
            courseAdapter.chapterList = chapters
            courseAdapter.notifyDataSetChanged()
        }
    }

}