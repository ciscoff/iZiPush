package s.yarlykov.pushsdk.utils

import android.content.Context
import android.util.TypedValue

/**
 * Конвертация dp в px. px возвращается как Float
 */
fun Context.dpToFloat(dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
    )
}

/**
 * Конвертация dp в px. px возвращается как Int
 */
fun Context.dpToInt(dp: Float): Int {
    return dpToFloat(dp).toInt()
}