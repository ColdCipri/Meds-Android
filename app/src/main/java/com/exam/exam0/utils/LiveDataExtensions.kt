package com.exam.exam0.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Extension method used for observing a non null [LiveData].
 */
fun <T> LiveData<T>.observeNonNull(lifecycleOwner: LifecycleOwner, action: (T) -> Unit) = observe(
    lifecycleOwner, Observer { it?.let(action) })