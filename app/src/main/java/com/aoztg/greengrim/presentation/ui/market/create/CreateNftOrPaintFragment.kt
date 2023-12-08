package com.aoztg.greengrim.presentation.ui.market.create

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateNftOrPaintBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateNftOrPaintFragment :
    BaseFragment<FragmentCreateNftOrPaintBinding>(R.layout.fragment_create_nft_or_paint) {

    private val parentViewModel: MainViewModel by activityViewModels()
    private var createType = CREATE_GRIM
    private lateinit var selectedLayout: ConstraintLayout

    companion object {
        const val CREATE_GRIM = 0
        const val CREATE_NFT = 1
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
        selectedLayout = binding.layoutCreateGrim
        setBtnClickListener()
    }

    private fun setBtnClickListener() {
        with(binding) {
            layoutCreateGrim.setOnClickListener {
                selectedLayout.setBackgroundResource(R.drawable.shape_lightblack2fill_nostroke_10radius)
                layoutCreateGrim.setBackgroundResource(R.drawable.shape_lightblack2fill_whitestroke_10radius)
                selectedLayout = layoutCreateGrim
                createType = CREATE_GRIM
            }

            layoutCreateNft.setOnClickListener {
                selectedLayout.setBackgroundResource(R.drawable.shape_lightblack2fill_nostroke_10radius)
                layoutCreateNft.setBackgroundResource(R.drawable.shape_lightblack2fill_whitestroke_10radius)
                selectedLayout = layoutCreateNft
                createType = CREATE_NFT
            }

            btnNext.setOnClickListener {
                when (createType) {
                    CREATE_GRIM -> findNavController().toCreateGrim()
                    CREATE_NFT -> findNavController().toCreateNft()
                }
            }

            btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun NavController.toCreateGrim() {
        val action =
            CreateNftOrPaintFragmentDirections.actionCreateNftOrPaintFragmentToCreatePaintFragment()
        navigate(action)
    }

    private fun NavController.toCreateNft() {
        val action =
            CreateNftOrPaintFragmentDirections.actionCreateNftOrPaintFragmentToCreateNftFragment()
        navigate(action)
    }
}