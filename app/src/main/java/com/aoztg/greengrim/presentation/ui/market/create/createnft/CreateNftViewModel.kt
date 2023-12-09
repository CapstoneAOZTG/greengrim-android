package com.aoztg.greengrim.presentation.ui.market.create.createnft

import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.data.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateNftViewModel @Inject constructor(
    private val nftRepository: NftRepository
): ViewModel() {


}