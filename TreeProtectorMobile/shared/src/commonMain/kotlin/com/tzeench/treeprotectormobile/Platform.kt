package com.tzeench.treeprotectormobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform