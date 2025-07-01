package com.wanandroid.app.ui.home.child.square

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wanandroid.app.base.BaseFragment
import com.wanandroid.app.databinding.FragmentHomeChildSquareBinding

class SquareFragment : BaseFragment<FragmentHomeChildSquareBinding>() {

    companion object {
        const val KEY_CHILD_SQUARE_TAB_PARCELABLE = "key_child_square_tab_parcelable"

        // Uncomment and implement if needed
        // fun newInstance(tabBean: HomeTabBean): SquareFragment {
        //     val fragment = SquareFragment()
        //     val args = Bundle().apply {
        //         putParcelable(KEY_CHILD_SQUARE_TAB_PARCELABLE, tabBean)
        //     }
        //     fragment.arguments = args
        //     return fragment
        // }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeChildSquareBinding.inflate(inflater, container, false)
        return binding.root
    }
}