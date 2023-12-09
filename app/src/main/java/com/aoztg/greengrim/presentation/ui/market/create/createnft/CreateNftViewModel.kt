package com.aoztg.greengrim.presentation.ui.market.create.createnft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class CreateNftViewModel @Inject constructor(
): ViewModel() {

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val imgUrl = MutableStateFlow("")

    val isDataReady = combine(title, description, imgUrl){ title, description, imgUrl ->
        title.isNotBlank() && description.isNotBlank() && imgUrl.isNotBlank()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        false
    )

    fun setImgUrl(url: String){
        imgUrl.value= url
    }

}