package io.bewsys.spmobile.util

import android.content.Context
import android.view.View
import android.widget.Button


inline fun Button.onButtonClicked(crossinline action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        override fun onClick(v: View?) {
            action()
        }
    })
}
//val Context.dataStore by preferencesDataStore(
//    name = USER_PREFERENCES_NAME, produceMigrations = { context ->
//        listOf(SharedPreferencesMigration(context, USER_PREFERENCES_NAME))
//    }
//)

//fun Context.getPreferences(preferenceKey: String): String? {
//    PreferenceManager.getDefaultSharedPreferences(this).apply {
//        return getString(preferenceKey, "")
//    }
//}


