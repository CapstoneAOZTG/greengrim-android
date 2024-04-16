package com.aoztg.greengrim.presentation.ui.mypage.mypoint

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyPointBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.mypage.adapter.MyPointAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPointFragment: BaseFragment<FragmentMyPointBinding>(R.layout.fragment_my_point) {

    private val viewModel : MyPointViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        setRecycler()
    }

    private fun setRecycler(){
        binding.rvPoint.adapter = MyPointAdapter()

        binding.rvPoint.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getMyPoint()
                }
            }
        })
    }
}