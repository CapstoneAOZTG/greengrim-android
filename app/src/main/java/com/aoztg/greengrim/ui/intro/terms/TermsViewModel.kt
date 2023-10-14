package com.aoztg.greengrim.ui.intro.terms

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TermsViewModel : ViewModel() {

    private val _isFirstClicked : MutableLiveData<Boolean> = MutableLiveData(false)
    val isFirstClicked : LiveData<Boolean> = _isFirstClicked

    private val _isSecondClicked : MutableLiveData<Boolean> = MutableLiveData(false)
    val isSecondClicked : LiveData<Boolean> = _isSecondClicked

    private val _isAllClicked : MutableLiveData<Boolean> = MutableLiveData(false)
    val isAllClicked : LiveData<Boolean> = _isAllClicked


    fun onClickFirst(){
        _isFirstClicked.value?.let{
            _isFirstClicked.value = !it
        }
    }

    fun onClickSecond(){
        _isSecondClicked.value?.let{
            _isSecondClicked.value = !it
        }
    }

    fun onClickAll(){
        _isAllClicked.value?.let{
            _isAllClicked.value = !it
        }
    }


}