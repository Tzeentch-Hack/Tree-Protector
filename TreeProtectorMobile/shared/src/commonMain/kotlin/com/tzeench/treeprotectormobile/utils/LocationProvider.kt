package com.tzeench.treeprotectormobile.utils

expect class LocationProvider() {
    fun fetchLocation(onResult: (String) -> Unit)
}