package com.aoztg.greengrim.presentation.ui.catchgame

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCatchIngameFragmentBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.customview.CatchGameOverDialog

class CatchInGameFragment: BaseFragment<FragmentCatchIngameFragmentBinding>(R.layout.fragment_catch_ingame_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }



    private fun exitGame(){
        findNavController().navigateUp()
    }

}