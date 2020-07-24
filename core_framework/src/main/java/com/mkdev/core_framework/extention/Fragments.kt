package com.mkdev.core_framework.extention

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment

fun Fragment.popBackStack() {
    NavHostFragment.findNavController(this).popBackStack()
}