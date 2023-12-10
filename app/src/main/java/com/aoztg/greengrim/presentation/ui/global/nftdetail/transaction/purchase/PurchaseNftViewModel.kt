package com.aoztg.greengrim.presentation.ui.global.nftdetail.transaction.purchase

import androidx.lifecycle.ViewModel
import com.aoztg.greengrim.data.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PurchaseNftViewModel @Inject constructor(
    private val nftRepository: NftRepository
): ViewModel() {

}