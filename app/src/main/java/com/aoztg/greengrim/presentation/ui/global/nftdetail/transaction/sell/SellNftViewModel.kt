package com.aoztg.greengrim.presentation.ui.global.nftdetail.transaction.sell

import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.data.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SellNftViewModel @Inject constructor(
    private val nftRepository: NftRepository
): ViewModel() {

}