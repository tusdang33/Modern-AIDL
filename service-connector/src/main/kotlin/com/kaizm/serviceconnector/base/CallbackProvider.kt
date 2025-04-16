package com.kaizm.serviceconnector.base

interface CallbackProvider<T> {

    val callbacks: MutableList<T>

    fun addCallback(cb: T) {
        callbacks.add(cb)
    }

    fun removeCallback(cb: T) {
        callbacks.remove(cb)
    }
}