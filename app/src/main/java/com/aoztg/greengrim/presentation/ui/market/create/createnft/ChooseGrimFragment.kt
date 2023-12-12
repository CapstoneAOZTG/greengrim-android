package com.aoztg.greengrim.presentation.ui.market.create.createnft

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentChooseGrimBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import com.aoztg.greengrim.presentation.ui.market.adapter.ChooseGrimAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChooseGrimFragment : BaseFragment<FragmentChooseGrimBinding>(R.layout.fragment_choose_grim) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private val viewModel: ChooseGrimViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        binding.vm = viewModel
        binding.rvMyGrim.adapter = ChooseGrimAdapter()
        viewModel.getMyGrimForNft()
        initEventObserver()
        setScrollEventListener()
    }

    private fun initEventObserver() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is ChooseGrimEvent.NavigateToBack -> findNavController().navigateUp()
                    is ChooseGrimEvent.NavigateToCreatePaint -> findNavController().toCreatePaint()
                    is ChooseGrimEvent.NavigateToCreateNft -> findNavController().toCreateNft(
                        it.grimId,
                        it.grimUrl
                    )

                    is ChooseGrimEvent.ShowSnackMessage -> showCustomSnack(binding.rvMyGrim, it.msg)
                    is ChooseGrimEvent.ShowNoGrimLayout -> binding.layoutNoGrim.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setScrollEventListener() {

        binding.rvMyGrim.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount?.minus(1)

                if (lastVisibleItemPosition == itemTotalCount) {
                    viewModel.getMyGrimForNft()
                }
            }
        })
    }

    private fun NavController.toCreateNft(grimId: Int, grimUrl: String) {
        val action =
            ChooseGrimFragmentDirections.actionChooseGrimFragmentToCreateNftFragment(
                grimId,
                grimUrl
            )
        navigate(action)
    }

    private fun NavController.toCreatePaint() {
        val action = ChooseGrimFragmentDirections.actionChooseGrimFragmentToCreatePaintFragment()
        navigate(action)
    }
}