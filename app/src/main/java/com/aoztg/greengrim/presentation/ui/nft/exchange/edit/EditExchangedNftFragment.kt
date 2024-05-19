package com.aoztg.greengrim.presentation.ui.nft.exchange.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentEditExchangedNftBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditExchangedNftFragment :
    BaseFragment<FragmentEditExchangedNftBinding>(R.layout.fragment_edit_exchanged_nft) {

    private val args: EditExchangedNftFragmentArgs by navArgs()
    private val imgUrl by lazy { args.imgUrl }
    private val nftId by lazy { args.nftId }

    private val viewModel: EditExchangedNftViewModel by viewModels()
    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setInfo(imgUrl, nftId)
        binding.vm = viewModel
        initEventObserve()
    }

    private fun initEventObserve() {
        repeatOnStarted {
            viewModel.events.collect {
                when (it) {
                    is EditExchangedNftEvent.NavigateToBack -> findNavController().navigateUp()
                    is EditExchangedNftEvent.NavigateToNftDetail -> findNavController().editToNftDetail(
                        it.id
                    )

                    is EditExchangedNftEvent.ShowCustomSnack -> parentViewModel.showSnack(it.msg)

                    is EditExchangedNftEvent.ShowToastMessage -> showCustomToast(it.msg)
                }
            }
        }
    }

    private fun NavController.editToNftDetail(id: Long) {
        val action =
            EditExchangedNftFragmentDirections.actionEditExchangedNftFragmentToNftDetailFragment(id)
        navigate(action)
    }

}