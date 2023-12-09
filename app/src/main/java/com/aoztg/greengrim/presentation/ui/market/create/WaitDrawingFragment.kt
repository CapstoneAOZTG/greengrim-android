package com.aoztg.greengrim.presentation.ui.market.create


import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentWaitDrawaingBinding
import com.aoztg.greengrim.presentation.base.BaseFragment

class WaitDrawingFragment :
    BaseFragment<FragmentWaitDrawaingBinding>(R.layout.fragment_wait_drawaing) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnExit.setOnClickListener {
            findNavController().toMarket()
        }
    }

    private fun NavController.toMarket() {
        val action = WaitDrawingFragmentDirections.actionWaitDrawingFragmentToMarketFragment()
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.main_nav, true).build()
        navigate(action, navOptions)
    }

}