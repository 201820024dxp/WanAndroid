package com.wanandroid.app.ui.home.child.answer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentAnswerBinding

class AnswerFragment : BaseFragment<FragmentAnswerBinding>() {

    companion object {
        const val KEY_CHILD_ANSWER_TAB_PARCELABLE = "key_child_answer_tab_parcelable"

        fun newInstance(): AnswerFragment {
            return AnswerFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnswerBinding.inflate(layoutInflater)
        return binding.root
    }

}