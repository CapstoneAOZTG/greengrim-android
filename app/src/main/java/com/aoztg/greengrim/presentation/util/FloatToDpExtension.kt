package com.aoztg.greengrim.presentation.util

import android.content.res.Resources

internal fun Float.toDp(): Float {
    val density = Resources.getSystem().displayMetrics.density
    return this / density
}