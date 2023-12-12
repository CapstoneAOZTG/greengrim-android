package com.aoztg.greengrim.presentation.ui.catchgame

import androidx.activity.viewModels
import com.aoztg.greengrim.databinding.ActivityCatchGameBinding
import com.aoztg.greengrim.presentation.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatchGameActivity: BaseActivity<ActivityCatchGameBinding>(ActivityCatchGameBinding::inflate) {

    private val viewModel: CatchGameViewModel by viewModels()

}