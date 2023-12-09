package com.aoztg.greengrim.presentation.bindingadapters

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

@BindingAdapter("layoutVisibility")
fun <T>bindLayoutVisibility(layout: ConstraintLayout, list: List<T>){
    if(list.isEmpty()){
        layout.visibility = View.VISIBLE
    } else {
        layout.visibility = View.INVISIBLE
    }
}