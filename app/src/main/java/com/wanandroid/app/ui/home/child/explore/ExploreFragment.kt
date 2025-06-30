package com.wanandroid.app.ui.home.child.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentExploreBinding

class ExploreFragment : BaseFragment<FragmentExploreBinding>() {

    companion object {
        const val KEY_CHILD_EXPLORE_TAB_PARCELABLE = "key_child_explore_tab_parcelable"

//        fun newInstance(tabBean: HomeTabBean): ExploreFragment {
//            val fragment = ExploreFragment()
//            val args = Bundle().apply {
//                putParcelable(KEY_CHILD_EXPLORE_TAB_PARCELABLE, tabBean)
//            }
//            fragment.arguments = args
//            return fragment
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the view
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}