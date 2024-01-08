package com.kentreyhan.commons.utils

import android.content.Context
import android.util.DisplayMetrics

fun Context.dpToPixel(dp: Float): Float {
    return dp * (resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}
