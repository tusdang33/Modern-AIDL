package com.kaizm.modernaidl

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun pushScreen(
    activity: FragmentActivity,
    destination: Fragment,
    container: Int = R.id.main_container,
    isAddToBackStack: Boolean = true
) {
    activity.supportFragmentManager.beginTransaction().apply {
        replace(container, destination)
        if (isAddToBackStack) addToBackStack(destination::class.java.name)
    }.commit()
}