package com.aoztg.greengrim.presentation.ui.challenge.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.aoztg.greengrim.R
import com.aoztg.greengrim.databinding.FragmentCreateChallengeBinding
import com.aoztg.greengrim.presentation.base.BaseFragment
import com.aoztg.greengrim.presentation.ui.main.MainViewModel

class CreateChallengeFragment : BaseFragment<FragmentCreateChallengeBinding>(R.layout.fragment_create_challenge){

    private val parentViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentViewModel.hideBNV()
    }
}