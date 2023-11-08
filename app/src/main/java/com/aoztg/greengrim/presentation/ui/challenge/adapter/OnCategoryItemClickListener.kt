package com.aoztg.greengrim.presentation.ui.challenge.adapter

import android.view.View
import com.aoztg.greengrim.presentation.ui.challenge.model.CategoryName

interface OnCategoryItemClickListener {

    fun onItemClicked(view: View, category: CategoryName)
}