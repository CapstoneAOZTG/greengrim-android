package com.aoztg.greengrim.customview

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView

class GgBottomNavigationView @JvmOverloads constructor(
    context : Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BottomNavigationView(context, attrs, defStyleAttr) {

    init{
        val menuView = getChildAt(0) as ViewGroup
        menuView.getChildAt(2).isClickable = false
    }
}