package io.bewsys.spmobile.util

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.preference.PreferenceManager


//inline fun Button.onButtonClicked(crossinline action: () -> Unit) {
//    this.setOnClickListener(object : View.OnClickListener {
//        override fun onClick(v: View?) {
//            action()
//        }
//    })
//}

fun Context.getPreferences(preferenceKey: String): String? {
    PreferenceManager.getDefaultSharedPreferences(this).apply {
        return getString(preferenceKey, "")
    }
}

fun MutableList<String>.swap(it: List<String>) {
    this.clear()
    this.addAll(it)
}

