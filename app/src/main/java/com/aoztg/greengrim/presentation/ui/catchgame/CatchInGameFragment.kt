package com.aoztg.greengrim.presentation.ui.catchgame

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCatchIngameFragmentBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatchInGameFragment: BaseFragment<FragmentCatchIngameFragmentBinding>(R.layout.fragment_catch_ingame_fragment) {

    private val viewModel: CatchInGameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    private fun exitGame(){
        findNavController().navigateUp()
    }

}