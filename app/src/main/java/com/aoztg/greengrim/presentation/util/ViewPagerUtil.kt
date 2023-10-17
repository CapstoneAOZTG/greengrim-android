package com.aoztg.greengrim.presentation.util

import android.view.View
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs


object ViewPagerUtil{

    fun getTransform() : CompositePageTransformer {
        val transform = CompositePageTransformer()
        transform.addTransformer(MarginPageTransformer(100))

        transform.addTransformer { view: View, fl: Float ->
            val v = 1 - abs(fl)
            view.scaleY = 0.8f + v * 0.2f
        }

        return transform
    }
}
