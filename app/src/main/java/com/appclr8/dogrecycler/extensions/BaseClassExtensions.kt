package com.appclr8.dogrecycler.extensions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import timber.log.Timber

/**
 * Close the keyboard widget
 * Check if no view has focus:
 * @param activity
 * @return true if keyboard was showing before hiding the keyboard
 */
fun Fragment.hideKeyboard() : Boolean {
    Timber.d("Fragment.hideKeyboard()")
    if (context != null && view != null) {
        Timber.d("context!=null && view!=null")
        val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
    return false
}

inline fun <reified T : Enum<T>> Intent.putEnumExtra(victim: T): Intent =
    putExtra(T::class.qualifiedName, victim.ordinal)

fun Bundle.putEnum(key: String, enum: Enum<*>){
    putString(key, enum.name)
}

inline fun <reified T : Enum<T>> Bundle.getEnum(key: String): T? {
    val stringVal = getString(key)
    return if(stringVal!=null)
        enumValueOf<T>(stringVal)
    else
        null
}

inline fun <reified T : Enum<T>> Intent.getEnumExtra(): T? =
    getIntExtra(T::class.qualifiedName, -1)
        .takeUnless { it == -1 }
        ?.let { T::class.java.enumConstants[it] }