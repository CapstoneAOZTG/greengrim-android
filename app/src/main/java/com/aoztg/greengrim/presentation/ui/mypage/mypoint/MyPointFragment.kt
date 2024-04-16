package com.aoztg.greengrim.presentation.ui.mypage.mypoint

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentMyPointBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.challenge.list.ChallengeListViewModel
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.mypage.adapter.MyPointAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPointFragment: BaseFragment<FragmentMyPointBinding>(R.layout.fragment_my_point) {

    private val viewModel : MyPointViewModel by viewModels()
    private val parentViewModel : MainViewModel by activityViewModels()

    private val args : MyPointFragmentArgs by navArgs()
    val name by lazy { args.name }
    val totalPoint by lazy { args.totalPoint }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vm = viewModel
        setRecycler()
        viewModel.setInfo(name,totalPoint)
        viewModel.getMyPoint()

        initEventObserve()
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

    private fun initEventObserve(){
        repeatOnStarted {
            viewModel.event.collect{
                when(it){
                    is MyPointEvent.ShowCustomSnack -> showCustomSnack(binding.ivBadge, it.msg)
                    is MyPointEvent.NavigateToBack -> findNavController().navigateUp()
                }
            }
        }
    }
}