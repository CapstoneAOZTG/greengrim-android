package com.aoztg.greengrim.presentation.ui.market.create.createnft

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aoztg.greengrim.data.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class CreateNftEvents{
    data class CreateNft(
        val title: String,
        val description: String
    ): CreateNftEvents()

    object NavigateToBack: CreateNftEvents()
}

@HiltViewModel
class CreateNftViewModel @Inject constructor(
): ViewModel() {

    private val _events = MutableSharedFlow<CreateNftEvents>()
    val events: SharedFlow<CreateNftEvents> = _events.asSharedFlow()

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

    fun createNft(){
        viewModelScope.launch {
            _events.emit(CreateNftEvents.CreateNft(
                title = title.value,
                description = description.value
            ))
        }
    }

    fun navigateToBack(){
        viewModelScope.launch {
            _events.emit(CreateNftEvents.NavigateToBack)
        }
    }

}