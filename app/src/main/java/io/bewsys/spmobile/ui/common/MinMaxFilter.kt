package io.bewsys.spmobile.ui.common

import android.text.InputFilter
import android.text.Spanned

class MinMaxFilter() : InputFilter {

    private var intMin: Int = 0
    private var intMax: Int = 0

    // Initialized
    constructor(minValue: Int, maxValue: Int) : this() {
        this.intMin = minValue
        this.intMax = maxValue
    }

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        try {
            val input = Integer.parseInt(dest.toString() + source.toString())
            if (isInRange(intMin, intMax, input)) {
                return null
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun isInRange(a: Int, b: Int, c: Int) = if (b > a) c in a..b else c in a..b

}